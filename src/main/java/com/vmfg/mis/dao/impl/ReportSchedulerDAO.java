package com.vmfg.mis.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.mis.dao.interfaces.iReportSchedulerDAO;
import com.vmfg.mis.entity.ReportSchedulerEntity;
import com.vmfg.mis.entity.TaskDtlEntity;
import com.vmfg.mis.rowmapper.GetOldTaskRowMapper;
import com.vmfg.mis.rowmapper.ReportSchedulerRowMapper;
import com.vmfg.project.dao.impl.ProjectDAO;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class ReportSchedulerDAO implements iReportSchedulerDAO {
	private static final Logger logger = LoggerFactory.getLogger(ProjectDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String getWeekStartedDate(String dateChk) {
		String resp = "";
		try {

			String getQ = "SELECT DATE_SUB( ? , INTERVAL WEEKDAY(?) + 1 DAY) AS START_WEEK;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ, dateChk, dateChk);
            resp = resultMap.get("START_WEEK").toString();
		} catch (Exception ex) {
			logger.error("getWeekStartedDate error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public String getTenantValue(String tenantId, String propertyValue) {
		return GetPropertyValue.getPropValueByTenant(tenantId, propertyValue, this.jdbcTemplate);
	}

	@Override
	public String getPrevDate(String dateChk) {
		String resp = "";
		try {

			String getQ = "SELECT DATE_FORMAT(? - INTERVAL 1 DAY, '%Y-%m-%d') AS PREVIOUS_DATE;";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ, dateChk);
            resp = resultMap.get("PREVIOUS_DATE").toString();
		} catch (Exception ex) {
			logger.error("getPrevDate error " + ex.getMessage());
		}
		return resp;
	}
	
	@Override
	public String getOrgTenant() {
		String resp = "";
		try {

			String getQ = "select group_concat(distinct(TENANT_ID)) AS TENANT_ID  from organization_info ";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(getQ);
            resp = resultMap.get("TENANT_ID").toString();
		} catch (Exception ex) {
			logger.error("getOrgTenant error " + ex.getMessage());
		}
		return resp;
	}
	

	@Override
	public List<ReportSchedulerEntity> getTaskDtl(String frmDate, String toDate,String tennatId) {
		List<ReportSchedulerEntity> list = new ArrayList<ReportSchedulerEntity>();
		try {
			String qry = "SELECT \r\n" + "    tdtl.ASSIGNED_TO,\r\n" + "    thdr.PM_HDR_ID,\r\n"
					+ "    thdr.DEPARTMENT_CODE,\r\n" + "    YEAR('" + toDate + "') AS REPORT_YEAR,\r\n" + "    MONTH('"
					+ toDate + "') AS REPORT_MONTH,\r\n" + "    '" + frmDate + "' AS WEEK_START,\r\n"
					+ "    COUNT(tdtl.PLANNED_START_DATE IS NOT NULL) AS NO_PLANNED_TASK,\r\n" + "    SUM(CASE\r\n"
					+ "        WHEN\r\n" + "            tdtl.COMPLETED_DATE IS NOT NULL\r\n"
					+ "                AND tdtl.IS_COMPLETED = '1'\r\n" + "        THEN\r\n" + "            1\r\n"
					+ "        ELSE 0\r\n" + "    END) AS NO_COMPLETED_TASK,\r\n" + "    SUM(CASE\r\n"
					+ "        WHEN\r\n"
					+ "            (tdtl.DUE_DATE < tdtl.COMPLETED_DATE OR (tdtl.COMPLETED_DATE IS NULL && tdtl.DUE_DATE < current_date()))    \r\n"
					+ "        THEN\r\n" + "            1\r\n" + "        ELSE 0\r\n" + "    END) AS DELAY_TASK,\r\n"
					+ "    CASE\r\n" + "        WHEN\r\n" + "            COUNT(CASE\r\n"
					+ "                WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\r\n" + "            END) > 0\r\n"
					+ "        THEN\r\n" + "            ROUND((SUM(CASE\r\n"
					+ "                        WHEN tdtl.COMPLETED_DATE IS NOT NULL THEN 1\r\n"
					+ "                        ELSE 0\r\n" + "                    END) / COUNT(CASE\r\n"
					+ "                        WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\r\n"
					+ "                    END)) * 100,\r\n" + "                    2)\r\n" + "        ELSE 0\r\n"
					+ "    END AS COMPLETION_PERCENTAGE,\r\n" + "    tdtl.TENANT_ID\r\n" + "FROM\r\n"
					+ "    task_entry_hdr thdr\r\n" + "        INNER JOIN\r\n"
					+ "    task_entry_dtl tdtl ON thdr.TE_HDR_ID = tdtl.TE_HDR_ID\r\n" + "WHERE\r\n"
					+ "    thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + "        AND tdtl.PLANNED_START_DATE IS NOT NULL\r\n"
					+ "        AND tdtl.DUE_DATE = '" + toDate + "' AND thdr.TENANT_ID = '"+tennatId+"' \r\n"
					+ "GROUP BY tdtl.ASSIGNED_TO , thdr.PM_HDR_ID , thdr.DEPARTMENT_CODE , YEAR(tdtl.DUE_DATE) , MONTH(tdtl.DUE_DATE) , DAY(tdtl.DUE_DATE)\r\n"
					+ "ORDER BY thdr.PM_HDR_ID , DATE_SUB(DATE_SUB(CURDATE(), INTERVAL 1 DAY),\r\n"
					+ "    INTERVAL WEEKDAY(DATE_SUB(CURDATE(), INTERVAL 1 DAY)) DAY);";

			list = this.jdbcTemplate.query(qry, new ReportSchedulerRowMapper());

		} catch (Exception ex) {
			logger.error("getTaskDtl Method Exception --->" + ex);
		}
		return list;
	}

	@Override
	public int checkReportDtl(String dayStart, String empId, String projId, String tenantID, String deptCode) {

		int resp = 0;
		try {
			String Checkquery = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(RT_ID) > 0 THEN RT_ID\r\n"
					+ "        ELSE 0\r\n" + "    END AS RT_ID\r\n" + "FROM\r\n" + "    report_task_workload\r\n"
					+ "WHERE\r\n" + "    REPORT_DATE = ?\r\n" + "        AND EMPLOYEE_ID = ? \r\n"  
					+ " AND PM_HDR_ID = ?\r\n" + " AND DEPARTMENT_CODE = ? \r\n"
					+ " AND TENANT_ID = ?;";

		    Map<String, Object> resultMap = jdbcTemplate.queryForMap(Checkquery, dayStart, empId, projId, deptCode, tenantID);
            resp = Integer.parseInt(resultMap.get("RT_ID").toString());
		} catch (Exception ex) {
			logger.error("checkReportDtl is method is error" + ex);
		}
		return resp;
	}

	@Override
	public int InsertRecordDtl(String empId, String projId, String deptCode, String year, String month, String dayStart,
			String noPlanned, String noCompleted, String delay, String perCentage, String tenantID, String reportDate) {
		int resp = 0;
		try {

			String insVendarMst = "INSERT INTO report_task_workload(EMPLOYEE_ID,\r\n"
					+ "        PM_HDR_ID,DEPARTMENT_CODE,REPORT_YEAR,REPORT_MONTH,WEEK_START,\r\n"
					+ "        NO_PLANNED_TASK,NO_COMPLETED_TASK,DELAY_TASK,PERCENTAGE_COMPLETED,TENANT_ID,REPORT_DATE)\r\n"
					+ "        VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
			KeyHolder holder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insVendarMst, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, empId);
					ps.setString(2, projId);
					ps.setString(3, deptCode);
					ps.setString(4, year);
					ps.setString(5, month);
					ps.setString(6, dayStart);
					ps.setString(7, noPlanned);
					ps.setString(8, noCompleted);
					ps.setString(9, delay);
					ps.setString(10, perCentage);
					ps.setString(11, tenantID);
					ps.setString(12, reportDate);
					return ps;
				}
			}, holder);
			int LocId = holder.getKey().intValue();
			resp = LocId;
		} catch (Exception ex) {
			logger.error("InsertRecordDtl is method is error" + ex);
		}
		return resp;
	}

	@Override
	public int UpdateRecordDtl(String empId, String projId, String deptCode, String year, String month, String dayStart,
			String noPlanned, String noCompleted, String delay, String perCentage, String tenantID, String reportDate) {

		int resp = 0;
		try {

			String updateRecord = "update report_task_workload set NO_PLANNED_TASK =? , NO_COMPLETED_TASK=?,DELAY_TASK=? ,PERCENTAGE_COMPLETED =? \r\n"
					+ "where EMPLOYEE_ID = ? and PM_HDR_ID =? and DEPARTMENT_CODE=? and REPORT_DATE=? and RT_ID>0;";

			int update = this.jdbcTemplate.update(updateRecord, noPlanned, noCompleted, delay, perCentage, empId,
					projId, deptCode, reportDate);
			if (update == 1) {
				resp = 1;
			}

		} catch (Exception ex) {
			logger.info("UpdateRecordDtl is method is error" + ex);
		}
		return resp;
	}

	@Override
	public List<TaskDtlEntity> getOldTaskCompleted(String refDate,String tenantId) {
		List<TaskDtlEntity> retLi = null;
		try {

			String getQ = "SELECT \r\n" + "    dtl.ASSIGNED_TO, hdr.PM_HDR_ID, hdr.DEPARTMENT_CODE, COUNT(dtl.TE_DTl_ID) as cnt\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr,\r\n" + "    task_entry_dtl as dtl\r\n" + "WHERE\r\n"
					+ "    hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + "        AND dtl.COMPLETED_DATE = ?\r\n"
					+ "        AND dtl.DUE_DATE <> ?\r\n" + "        AND hdr.DEPENDENT_TE_HDR_ID IS NULL\r\n"
					+ "        AND dtl.PLANNED_START_DATE IS NOT NULL AND hdr.TENANT_ID = ? \r\n"
					+ "GROUP BY dtl.ASSIGNED_TO ,hdr.DEPARTMENT_CODE, hdr.PM_HDR_ID";
			retLi = this.jdbcTemplate.query(getQ, new GetOldTaskRowMapper(), refDate, refDate,tenantId);

		} catch (Exception ex) {
			logger.info("getOldTaskPlanned is method is error" + ex);
		}
		return retLi;
	}

	@Override
	public List<TaskDtlEntity> getOldTaskPlanned(String refDate,String tenantId) {

		List<TaskDtlEntity> retLi = null;
		try {

			String getQ = "SELECT \r\n" + "    dtl.ASSIGNED_TO, hdr.PM_HDR_ID, hdr.DEPARTMENT_CODE, COUNT(dtl.TE_DTl_ID) as cnt\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr,\r\n" + "    task_entry_dtl as dtl \r\n" + "WHERE\r\n"
					+ "    hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + "        AND  dtl.IS_COMPLETED = '0' AND dtl.DUE_DATE < ?\r\n"
					+ "        AND hdr.DEPENDENT_TE_HDR_ID IS NULL\r\n"
					+ "        AND dtl.PLANNED_START_DATE IS NOT NULL AND hdr.TENANT_ID = ? \r\n"
					+ "GROUP BY dtl.ASSIGNED_TO, hdr.DEPARTMENT_CODE, hdr.PM_HDR_ID";
			retLi = this.jdbcTemplate.query(getQ, new GetOldTaskRowMapper(), refDate,tenantId);

		} catch (Exception ex) {
			logger.info("getOldTaskCompleted is method is error" + ex);
		}
		return retLi;

	}

	@Override
	public void updatePlannedTask(TaskDtlEntity taskDtlEntity, String recordDate,String tenantId) {

		try {

			String updateRecord = "update report_task_workload set NO_PLANNED_TASK = NO_PLANNED_TASK+ ? where EMPLOYEE_ID =? and PM_HDR_ID = ? and DEPARTMENT_CODE =? and REPORT_DATE = ? and RT_ID>0 and TENANT_ID = ? ";

			this.jdbcTemplate.update(updateRecord, taskDtlEntity.getCnt(), taskDtlEntity.getAssignedTo(),
					taskDtlEntity.getPmHdrid(), taskDtlEntity.getDeptCode(), recordDate,tenantId);

		} catch (Exception ex) {
			logger.info("updatePlannedTask is method is error" + ex);
		}

	}

	@Override
	public void updateCompTask(TaskDtlEntity taskDtlEntity, String refDate,String tenantId) {
		try {

			String updateRecord = "update report_task_workload set NO_COMPLETED_TASK = NO_COMPLETED_TASK+ ? where EMPLOYEE_ID =? and PM_HDR_ID = ? and DEPARTMENT_CODE =? and REPORT_DATE = ? and RT_ID>0 and TENANT_ID = ? ";

			this.jdbcTemplate.update(updateRecord, taskDtlEntity.getCnt(), taskDtlEntity.getAssignedTo(),
					taskDtlEntity.getPmHdrid(), taskDtlEntity.getDeptCode(), refDate,tenantId);

		} catch (Exception ex) {
			logger.info("updateCompTask is method is error" + ex);
		}

	}

	@Override
	public void taskPercentUpdate(String refDate,String tenantId) {
		try {

			String updateRecord = "update report_task_workload set PERCENTAGE_COMPLETED = ((NO_COMPLETED_TASK / NO_PLANNED_TASK)*100) where REPORT_DATE = ? and RT_ID>0 and TENANT_ID = ?";

			this.jdbcTemplate.update(updateRecord, refDate,tenantId);

		} catch (Exception ex) {
			logger.info("taskPercentUpdate is method is error" + ex);
		}

	}

}
