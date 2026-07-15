package com.vmfg.timesheet.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.timesheet.dao.interfaces.ITimeSheetDAO;
import com.vmfg.timesheet.request.DeleteActivityforEmpIdRequest;
import com.vmfg.timesheet.request.DeleteTimeSheetTaskDtlRequest;
import com.vmfg.timesheet.request.GetAllTimeSheetTaskForHdrEntity;
import com.vmfg.timesheet.request.GetAllTimeSheetTaskForHdrRequest;
import com.vmfg.timesheet.request.GetTimeDtlByHdrRequest;
import com.vmfg.timesheet.request.GetTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.GetTimeSheetTaskForHdrandDtlRequset;
import com.vmfg.timesheet.request.InsertIndividualTimeSheetTaskDtl;
import com.vmfg.timesheet.request.InsertTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.InsertTimeSheetDtlRequest;
import com.vmfg.timesheet.request.InsertTimeSheetRequest;
import com.vmfg.timesheet.request.InsertTimeSheetTaskDtlRequest;
import com.vmfg.timesheet.request.InsertTimeSheetTaskRequest;
import com.vmfg.timesheet.request.TaskEntryHdrAndDtl;
import com.vmfg.timesheet.request.TimeSheetRemainingLogRequest;
import com.vmfg.timesheet.request.TimeSheetRequests;
import com.vmfg.timesheet.request.UpdateTimeSheetCategoryRequest;
import com.vmfg.timesheet.request.UpdateTimeSheetTaskHdrGroupName;
import com.vmfg.timesheet.request.UpdateTimeSheetTaskRequest;
import com.vmfg.timesheet.response.GetEmpIDinTimesheetResponse;
import com.vmfg.timesheet.response.GetTimeDtlByHdrEntity;
import com.vmfg.timesheet.response.GetTimeSheetCategoryEntity;
import com.vmfg.timesheet.response.GetTimeSheetDtlEntity;
import com.vmfg.timesheet.response.GetTimeSheetTaskForHdrandDtlEntity;
import com.vmfg.timesheet.response.TaskEntryHdrAndDtlEntity;
import com.vmfg.timesheet.response.TimeSheetDeptDtlEntity;
import com.vmfg.timesheet.response.TimeSheetMonthDtlEntity;
import com.vmfg.timesheet.response.TimeSheetRequestsEntity;
import com.vmfg.timesheet.rowmapper.GetAllTimeSheetTaskForHdrRowMapper;
import com.vmfg.timesheet.rowmapper.GetEmpIDinTimesheetRowMapper;
import com.vmfg.timesheet.rowmapper.GetTimeDtlByHdrRowMapper;
import com.vmfg.timesheet.rowmapper.GetTimeSheetCategoryRowMapper;
import com.vmfg.timesheet.rowmapper.GetTimeSheetDtlRowMapper;
import com.vmfg.timesheet.rowmapper.GetTimeSheetTaskForHdrandDtlRowMapper;
import com.vmfg.timesheet.rowmapper.TaskEntryHdrAndDtlRowMapper;
import com.vmfg.timesheet.rowmapper.TimeSheetDeptDtlRowMapper;
import com.vmfg.timesheet.rowmapper.TimeSheetMonthDtlRowMapper;
import com.vmfg.timesheet.rowmapper.TimeSheetRequestsRowMapper;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class TimeSheetDAO implements ITimeSheetDAO {
	private static final Logger logger = LoggerFactory.getLogger(TimeSheetDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private UploadManagementDAO uploadManagementDAO;
	

	@Override
	public String timesheetProjectInitiationPoc(String empId, String tenantId) {
		String res = "";
		try {
			String desig = uploadManagementDAO.getDesigCodeByEmpId(empId, tenantId);
			String qrySelect = " SELECT     COUNT(*) as COUNT FROM project_wbs_initiation_mst\r\n" + "	WHERE   FIND_IN_SET(?, MASTER_POC) and TENANT_ID=?";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qrySelect, desig,tenantId);
			int count = Integer.parseInt(resultMap.get("COUNT").toString());
			if (count > 0) {
				res = "1";
			} else {
				res = "0";
			}
		} catch (Exception ex) {
			logger.error("timesheetProjectInitiationPoc error " + ex.getMessage());
			res = "2";
		}
		return res;
	}

	@Override
	public List<GetEmpIDinTimesheetResponse> getEmpIDinTimesheet(String empId, String pmHdrId, String tenantId,
			String deptAssigned) {
		List<GetEmpIDinTimesheetResponse> list = new ArrayList<>();
		try {
			if (!deptAssigned.isEmpty()) {
				String qry = "SELECT \r\n" + "   distinct em.EMPLOYEE_ID, em.EMPLOYEE_CODE, em.EMPLOYEE_FIRSTNAME\r\n"
						+ "FROM\r\n" + "    timesheet_hdr thdr,\r\n" + "    project_hdr phdr,\r\n"
						+ "    employee_mst em,\r\n" + "    department dept\r\n" + "WHERE\r\n"
						+ "    em.EMPLOYEE_ID = thdr.EMPLOYEE_ID\r\n"
						+ "        AND dept.DEPARTMENT_CODE = em.DEPARTMENT_CODE\r\n"
						+ "        AND phdr.PM_HDR_ID = thdr.PM_HDR_ID\r\n" + "        AND thdr.PM_HDR_ID LIKE ?\r\n"
						+ "        AND FIND_IN_SET(dept.DEPARTMENT_CODE, ?)\r\n" + "        AND thdr.TENANT_ID = '"
						+ tenantId + "'";

				RowMapper<GetEmpIDinTimesheetResponse> rowmapper = new GetEmpIDinTimesheetRowMapper();
				list = this.jdbcTemplate.query(qry, rowmapper, pmHdrId, deptAssigned);
			} else {
				String qry = "SELECT \r\n" + " distinct  em.EMPLOYEE_ID,\r\n" + "    em.EMPLOYEE_CODE,\r\n"
						+ "    em.EMPLOYEE_FIRSTNAME\r\n" + "FROM\r\n" + "    timesheet_hdr thdr,\r\n"
						+ "    project_hdr phdr,\r\n" + "    employee_mst em,\r\n" + "    department dept\r\n"
						+ "WHERE\r\n" + "    em.EMPLOYEE_ID = thdr.EMPLOYEE_ID\r\n"
						+ "        AND dept.DEPARTMENT_CODE = em.DEPARTMENT_CODE\r\n"
						+ "        AND phdr.PM_HDR_ID = thdr.PM_HDR_ID\r\n" + "        AND thdr.EMPLOYEE_ID=?\r\n"
						+ "        AND thdr.PM_HDR_ID like ?\r\n" + "        AND thdr.TENANT_ID = '" + tenantId + "'";

				RowMapper<GetEmpIDinTimesheetResponse> rowmapper = new GetEmpIDinTimesheetRowMapper();
				list = this.jdbcTemplate.query(qry, rowmapper, empId, pmHdrId);
			}
		} catch (Exception ex) {
			logger.error("getEmpIDinTimesheet  method exception-->" + ex);
		}
		logger.debug("getEmpIDinTimesheet  method end");
		return list;
	}

	@Override
	public List<GetTimeSheetDtlEntity> getTimeSheetDtl(String assignedEmpId, String pmHdrId, String tenantId,
			String fromDate, String toDate, String deptAssigned) {
		List<GetTimeSheetDtlEntity> list = new ArrayList<>();
		String qry = "";
		try {
			if (!deptAssigned.isEmpty()) {
				
				qry="SELECT \r\n" + 
						"    phdr.PROJECT_NAME,\r\n" + 
						"    phdr.PM_HDR_ID,\r\n" + 
						"    phdr.PROJECT_CODE,\r\n" + 
						"    phdr.PROJECT_DESCRIPTION,\r\n" + 
						"    thdr.RECORDED_ON,\r\n" + 
						"    tdtl.TIMESHEET_DTL,\r\n" + 
						"    tdtl.T_DTL_ID,\r\n" + 
						"    dept.DEPARTMENT_NAME,\r\n" + 
						"    dept.DEPARTMENT_CODE,\r\n" + 
						"    em.EMPLOYEE_CODE,\r\n" + 
						"    em.EMPLOYEE_FIRSTNAME,\r\n" + 
						"    tdtl.TIMESHEET_HRS,\r\n" + 
						"    thdr.RECORD_DATE,\r\n" + 
						"    em.EMPLOYEE_ID,\r\n" + 
						"    cat.TC_NAME as TIMESHEET_CATEGORY,\r\n" + 
						"    thdr.SUMMARY,\r\n" + 
						"    tcm.TC_DESC as TYPE,\r\n" + 
						"    ttm.TT_DESC as CATEGORY,ted.TE_DTl_ID AS TE_DTl_ID ,ted.ACTIVITY_NAME AS TASK_ENTRY_DESC \r\n" + 
						"FROM \r\n" + 
						"    timesheet_hdr thdr\r\n" + 
						"INNER JOIN \r\n" + 
						"    timesheet_dtl tdtl ON thdr.T_HDR_ID = tdtl.T_HDR_ID\r\n" + 
						"INNER JOIN \r\n" + 
						"    project_hdr phdr ON phdr.PM_HDR_ID = thdr.PM_HDR_ID\r\n" + 
						"INNER JOIN \r\n" + 
						"    employee_mst em ON em.EMPLOYEE_ID = thdr.EMPLOYEE_ID\r\n" + 
						"INNER JOIN \r\n" + 
						"    department dept ON em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE\r\n" + 
						"INNER JOIN \r\n" + 
						"    timesheet_cateogry cat ON thdr.TC_ID = cat.TC_ID\r\n" + 
						"LEFT JOIN \r\n" + 
						"    task_category_mst tcm ON thdr.TC_CODE = tcm.TC_CODE\r\n" + 
						"LEFT JOIN \r\n" + 
						"    task_type_mst ttm ON tcm.TT_CODE = ttm.TT_CODE LEFT JOIN task_entry_dtl ted on ted.TE_DTl_ID = tdtl.TE_DTl_ID \r\n" + 
						"WHERE \r\n" + 
						"    thdr.PM_HDR_ID LIKE ?\r\n" + 
						"    AND FIND_IN_SET(dept.DEPARTMENT_CODE, ?)\r\n" + 
						"    AND thdr.TENANT_ID = '" + tenantId + "'\r\n" + 
						"    AND thdr.RECORD_DATE BETWEEN '" + fromDate + "' AND '" + toDate + "'\r\n" + 
						"";
				
//				qry = "SELECT \r\n" + "    phdr.PROJECT_NAME,\r\n" + "    phdr.PM_HDR_ID,\r\n"
//						+ "    phdr.PROJECT_CODE,\r\n" + "    phdr.PROJECT_DESCRIPTION,\r\n"
//						+ "    thdr.RECORDED_ON,\r\n" + "    tdtl.TIMESHEET_DTL,\r\n" + "  tdtl.T_DTL_ID,\r\n"
//						+ "   dept.DEPARTMENT_NAME,\r\n" + "    dept.DEPARTMENT_CODE,\r\n" + "    em.EMPLOYEE_CODE,\r\n"
//						+ "    em.EMPLOYEE_FIRSTNAME,\r\n" + "    tdtl.TIMESHEET_HRS,\r\n" + "    thdr.RECORD_DATE,\r\n"
//						+ "    em.EMPLOYEE_ID\r\n" + "FROM\r\n" + "    timesheet_hdr thdr,\r\n"
//						+ "    timesheet_dtl tdtl,\r\n" + "    project_hdr phdr,\r\n" + "    employee_mst em,\r\n"
//						+ "    department dept\r\n" + "WHERE\r\n" + "    phdr.PM_HDR_ID = thdr.PM_HDR_ID\r\n"
//						+ "        AND thdr.T_HDR_ID = tdtl.T_HDR_ID\r\n"
//						+ "        AND em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE AND em.EMPLOYEE_ID = thdr.EMPLOYEE_ID \r\n"
//						+ "        AND thdr.PM_HDR_ID like ?\r\n"
//						+ "        AND FIND_IN_SET(dept.DEPARTMENT_CODE, ?)\r\n" + "        AND thdr.TENANT_ID = '"
//						+ tenantId + "'\r\n" + "        AND thdr.RECORD_DATE BETWEEN '" + fromDate + "' AND '" + toDate
//						+ "'";
				RowMapper<GetTimeSheetDtlEntity> rowmapper = new GetTimeSheetDtlRowMapper();
				list = this.jdbcTemplate.query(qry, rowmapper, pmHdrId, deptAssigned);
			} else {
				qry="SELECT \r\n" + 
						"    phdr.PROJECT_NAME,\r\n" + 
						"    phdr.PM_HDR_ID,\r\n" + 
						"    phdr.PROJECT_CODE,\r\n" + 
						"    phdr.PROJECT_DESCRIPTION,\r\n" + 
						"    thdr.RECORDED_ON,\r\n" + 
						"    tdtl.TIMESHEET_DTL,\r\n" + 
						"    tdtl.T_DTL_ID,\r\n" + 
						"    dept.DEPARTMENT_NAME,\r\n" + 
						"    dept.DEPARTMENT_CODE,\r\n" + 
						"    em.EMPLOYEE_CODE,\r\n" + 
						"    em.EMPLOYEE_FIRSTNAME,\r\n" + 
						"    tdtl.TIMESHEET_HRS,\r\n" + 
						"    thdr.RECORD_DATE,\r\n" + 
						"    em.EMPLOYEE_ID,\r\n" + 
						"    cat.TC_NAME AS TIMESHEET_CATEGORY,\r\n" + 
						"    thdr.SUMMARY,\r\n" + 
						"    tcm.TC_DESC AS TYPE,\r\n" + 
						"    ttm.TT_DESC AS CATEGORY\r\n" + 
						"FROM \r\n" + 
						"    timesheet_hdr thdr\r\n" + 
						"INNER JOIN \r\n" + 
						"    timesheet_dtl tdtl ON thdr.T_HDR_ID = tdtl.T_HDR_ID\r\n" + 
						"INNER JOIN \r\n" + 
						"    project_hdr phdr ON phdr.PM_HDR_ID = thdr.PM_HDR_ID\r\n" + 
						"INNER JOIN \r\n" + 
						"    employee_mst em ON em.EMPLOYEE_ID = thdr.EMPLOYEE_ID\r\n" + 
						"INNER JOIN \r\n" + 
						"    department dept ON em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE\r\n" + 
						"INNER JOIN \r\n" + 
						"    timesheet_cateogry cat ON thdr.TC_ID = cat.TC_ID\r\n" + 
						"LEFT JOIN \r\n" + 
						"    task_category_mst tcm ON thdr.TC_CODE = tcm.TC_CODE\r\n" + 
						"LEFT JOIN \r\n" + 
						"    task_type_mst ttm ON tcm.TT_CODE = ttm.TT_CODE\r\n" + 
						"WHERE \r\n" + 
						"    thdr.PM_HDR_ID LIKE ?\r\n" + 
						"    AND thdr.EMPLOYEE_ID = ?\r\n" + 
						"    AND thdr.TENANT_ID = '" + tenantId + "'\r\n" + 
						"    AND thdr.RECORD_DATE BETWEEN '" + fromDate + "' AND '" + toDate + "'\r\n" + 
						"";
				
//				qry = "SELECT \r\n" + "    phdr.PROJECT_NAME,\r\n" + "    phdr.PM_HDR_ID,\r\n"
//						+ "    phdr.PROJECT_CODE,\r\n" + "    phdr.PROJECT_DESCRIPTION,\r\n"
//						+ "    thdr.RECORDED_ON,\r\n" + "    tdtl.TIMESHEET_DTL,\r\n" + "  tdtl.T_DTL_ID,\r\n"
//						+ "   dept.DEPARTMENT_NAME,\r\n" + "    dept.DEPARTMENT_CODE,\r\n" + "    em.EMPLOYEE_CODE,\r\n"
//						+ "    em.EMPLOYEE_FIRSTNAME,\r\n" + "    tdtl.TIMESHEET_HRS,\r\n" + "    thdr.RECORD_DATE,\r\n"
//						+ "    em.EMPLOYEE_ID\r\n" + "FROM\r\n" + "    timesheet_hdr thdr,\r\n"
//						+ "    timesheet_dtl tdtl,\r\n" + "    project_hdr phdr,\r\n" + "    employee_mst em,\r\n"
//						+ "    department dept\r\n" + "WHERE\r\n" + "    phdr.PM_HDR_ID = thdr.PM_HDR_ID\r\n"
//						+ "        AND thdr.T_HDR_ID = tdtl.T_HDR_ID\r\n"
//						+ "        AND em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE AND em.EMPLOYEE_ID = thdr.EMPLOYEE_ID \r\n"
//						+ "        AND thdr.PM_HDR_ID like ?\r\n" + "        AND thdr.EMPLOYEE_ID = ?\r\n"
//						+ "        AND thdr.TENANT_ID = '" + tenantId + "'\r\n"
//						+ "        AND thdr.RECORD_DATE BETWEEN '" + fromDate + "' AND '" + toDate + "'";
				
				RowMapper<GetTimeSheetDtlEntity> rowmapper = new GetTimeSheetDtlRowMapper();
				list = this.jdbcTemplate.query(qry, rowmapper, pmHdrId, assignedEmpId);
			}

		} catch (Exception ex) {
			logger.error("getTimeSheetDtl  method exception-->" + ex);
		}
		logger.debug("getTimeSheetDtl  method end");
		return list;
	}

	@Override
	public int insertTimeSheetHdr(InsertTimeSheetRequest insertTimeSheetDtl) {
		logger.debug("insertTimeSheetHdr   method Start");
		int insertRes = 0;
		try {
			String insertQ = "INSERT INTO timesheet_hdr (TC_ID, EMPLOYEE_ID, RECORD_DATE, TE_HDR_ID, PM_HDR_ID, TD_ID,TC_CODE, SUMMARY, RECORDED_ON, RECORDED_BY, TENANT_ID) "
					+ "VALUES (?,?,?,?,?,?,?,?,NOW(),?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, insertTimeSheetDtl.getTcId());
					ps.setString(2, insertTimeSheetDtl.getEmpId());
					ps.setString(3, insertTimeSheetDtl.getRecordDate());
					ps.setString(4, insertTimeSheetDtl.getTeHdrId());
					ps.setString(5, insertTimeSheetDtl.getPmHdrId());
					ps.setString(6, insertTimeSheetDtl.getTdId());
					ps.setString(7, insertTimeSheetDtl.getTaskCategoryCode());
					ps.setString(8, insertTimeSheetDtl.getSummary());
					ps.setString(9, insertTimeSheetDtl.getRecordedBy());
					ps.setString(10, insertTimeSheetDtl.getTenantId());

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertTimeSheetHdr  method  exception" + ex);
		}
		logger.debug("insertTimeSheetHdr   method end");
		return insertRes;
	}

	@Override
	public int insertTimeSheetDtl(int responseHdrId, InsertTimeSheetDtlRequest dtlObj, BigDecimal appSal,String reqDate,String empId) {
		int insertStatus = 0;
		try {
			BigDecimal overAllhrs = new BigDecimal(192); // 24 working days and 8 hrs of work

			
			BigDecimal timeSheetHrs = new BigDecimal(dtlObj.getTimeSheetHrs());
			if(timeSheetHrs.compareTo(new BigDecimal("8"))==1) {
				timeSheetHrs = new BigDecimal("8");
			}
			BigDecimal cost = (appSal.divide(overAllhrs,2, RoundingMode.DOWN)
					.multiply(timeSheetHrs));
			String qry = "INSERT INTO timesheet_dtl ( T_HDR_ID, TE_DTL_ID, TD_DTL_ID, TIMESHEET_DTL, TIMESHEET_HRS, TENANT_ID, TIMESHEET_COST) VALUES (?,?,?,?,?,?,?)";
			insertStatus = this.jdbcTemplate.update(qry, responseHdrId, dtlObj.getTeDtlId(), dtlObj.getTdDtlId(),
					dtlObj.getTimeSheetDtl(), dtlObj.getTimeSheetHrs(), dtlObj.getTenantId(), cost);
			timeSheetDtlCost(dtlObj,appSal,reqDate,empId);
		} catch (Exception ex) {
			logger.error("insertTimeSheetDtl method Error" + ex);
		}
		return insertStatus;
	}

	public int timeSheetDtlCost(InsertTimeSheetDtlRequest dtlObj, BigDecimal appSal,String reqDate,String empId) {
		int insertStatus = 0;
		try {
			String timeSheetDtlStr = "SELECT \n"
					+ "    ph.PROJECT_CODE,\n"
					+ "    taskHdr.RECORDED_BY,\n"
					+ "    ph.PROJECT_NAME,\n"
					+ "    taskHdr.RECORD_DATE,\n"
					+ "    taskDtl.TIMESHEET_DTL AS ACTIVITY,\n"
					+ "    em.EMPLOYEE_CODE,\n"
					+ "    em.EMPLOYEE_FIRSTNAME,\n"
					+ "    taskDtl.TIMESHEET_COST AS RUPEES,\n"
					+ "    taskDtl.TIMESHEET_HRS AS HOURS,taskDtl.T_DTL_ID AS T_DTL_ID \n"
					+ "FROM\n"
					+ "    timesheet_hdr taskHdr,\n"
					+ "    timesheet_dtl taskDtl,\n"
					+ "    project_hdr ph,\n"
					+ "    employee_mst em,\n"
					+ "    timesheet_cateogry tc\n"
					+ "WHERE\n"
					+ "    tc.TC_ID = taskHdr.TC_ID\n"
					+ "        AND taskHdr.EMPLOYEE_ID = em.EMPLOYEE_ID\n"
					+ "        AND taskHdr.PM_HDR_ID = ph.PM_HDR_ID\n"
					+ "        AND taskHdr.T_HDR_ID = taskDtl.T_HDR_ID\n"
					+ "        AND taskHdr.TENANT_ID = ? \n"
					+ "        AND taskHdr.RECORD_DATE = ? \n"
					+ "        AND taskHdr.EMPLOYEE_ID = ? ";
			RowMapper<TimeSheetRequestsEntity> rowmapper = new TimeSheetRequestsRowMapper();
			List<TimeSheetRequestsEntity> timeSheetDtl = this.jdbcTemplate.query(timeSheetDtlStr, rowmapper, dtlObj.getTenantId(), reqDate,empId);
			BigDecimal workingdays = new BigDecimal(24);
			if(timeSheetDtl.size()>0) {
				BigDecimal totalactHour = timeSheetDtl.stream().map(x -> new BigDecimal(x.getHrs()))
						.reduce(BigDecimal.ZERO, BigDecimal::add);
				if(totalactHour.compareTo(new BigDecimal("8")) > 0) {
					for(int i =0;i<timeSheetDtl.size();i++) {
						BigDecimal cost = (appSal.divide(workingdays,2, RoundingMode.DOWN)
								.multiply(new BigDecimal(timeSheetDtl.get(i).getHrs()).divide(totalactHour,2, RoundingMode.DOWN)));
						String updateDtl="UPDATE `timesheet_dtl` SET `TIMESHEET_COST`= ? WHERE `T_DTL_ID`= ? ";
						 this.jdbcTemplate.update(updateDtl, cost.intValue(),timeSheetDtl.get(i).getTimeSheetDtlId());
					}
				}
			}
			 // 24 working days and 8 hrs of work

			
//			BigDecimal timeSheetHrs = new BigDecimal(dtlObj.getTimeSheetHrs());
//			if(timeSheetHrs.compareTo(new BigDecimal("8"))==1) {
//				timeSheetHrs = new BigDecimal("8");
//			}
//			BigDecimal cost = (appSal.divide(workingdays,2, RoundingMode.DOWN)
//					.multiply(timeSheetHrs));
//			String qry = "INSERT INTO timesheet_dtl ( T_HDR_ID, TE_DTL_ID, TD_DTL_ID, TIMESHEET_DTL, TIMESHEET_HRS, TENANT_ID, TIMESHEET_COST) VALUES (?,?,?,?,?,?,?)";
//			insertStatus = this.jdbcTemplate.update(qry, responseHdrId, dtlObj.getTeDtlId(), dtlObj.getTdDtlId(),
//					dtlObj.getTimeSheetDtl(), dtlObj.getTimeSheetHrs(), dtlObj.getTenantId(), cost);

		} catch (Exception ex) {
			logger.error("insertTimeSheetDtl method Error" + ex);
		}
		return insertStatus;
	}
	@Override
	public List<GetTimeDtlByHdrEntity> getTimeDtlByHdr(GetTimeDtlByHdrRequest getTimeDtlByHdr) {
		List<GetTimeDtlByHdrEntity> list = new ArrayList<>();
		try {
			String qry = "select *from timesheet_dtl where T_HDR_ID='" + getTimeDtlByHdr.getThdrId()
					+ "' and TENANT_ID='" + getTimeDtlByHdr.getTenantId() + "'";

			RowMapper<GetTimeDtlByHdrEntity> rowmapper = new GetTimeDtlByHdrRowMapper();
			list = this.jdbcTemplate.query(qry, rowmapper);
		} catch (Exception ex) {
			logger.error("getTimeDtlByHdr  method exception-->" + ex);
		}
		logger.debug("getTimeDtlByHdr  method end");
		return list;
	}

	@Override
	public int deleteActivityforEmpId(String thdrId, DeleteActivityforEmpIdRequest deletereq) {
		int deleteDtl = 0;
		int deleteHdr = 0;
		try {

			String dtlQry = "delete from timesheet_dtl where T_DTL_ID=? and  TENANT_ID=?";
			String hdrQry = "delete from timesheet_hdr where T_HDR_ID=? and  TENANT_ID=?";
			deleteDtl = this.jdbcTemplate.update(dtlQry,deletereq.getTdtlId(),deletereq.getTenantId());
			if (deleteDtl > 0) {
				deleteHdr = this.jdbcTemplate.update(hdrQry,thdrId,deletereq.getTenantId());
			}

		} catch (Exception ex) {
			logger.error("deleteActivityforEmpId method Error" + ex);
		}
		return deleteHdr;
	}

	@Override
	public String gettHdrId(DeleteActivityforEmpIdRequest deletereq) {
		String id = "";
		try {
			String qry = "select T_HDR_ID from timesheet_dtl where T_DTL_ID= ? and TENANT_ID=? ";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,deletereq.getTdtlId(),deletereq.getTenantId());
			id = resultMap.get("T_HDR_ID").toString();

		} catch (Exception ex) {
			logger.error("gettHdrId  method exception-->" + ex);
		}
		logger.debug("gettHdrId  method end");
		return id;
	}

	@Override
	public int getCount(String thdrId) {
		int count = 0;
		try {
			String qry = "select count(*) as COUNT from timesheet_dtl where T_HDR_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, thdrId);
			count = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("getCount  method exception-->" + ex);
		}
		logger.debug("getCount  method end");
		return count;
	}

	@Override
	public int insertTimeSheetTaskHdr(InsertTimeSheetTaskRequest insertTimeSheetTask) {
		logger.debug("insertTimeSheetTaskHdr   method Start");
		int insertRes = 0;
		try {
			String insertQ = "INSERT INTO timesheet_tasks_hdr ( TASK_GROUP_NAME, CREATED_BY, CREATED_ON, UPDATED_ON, UPDATED_BY, TENANT_ID) "
					+ "VALUES (?,?,NOW(),NOW(),?,?)";

			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(insertQ, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, insertTimeSheetTask.getTaskGroupName());
					ps.setString(2, insertTimeSheetTask.getCreatedBy());
					ps.setString(3, insertTimeSheetTask.getUpdatedBy());
					ps.setString(4, insertTimeSheetTask.getTenantId());

					return ps;
				}

			}, holder);
			insertRes = holder.getKey().intValue();

		} catch (Exception ex) {
			logger.error("insertTimeSheetTaskHdr  method  exception" + ex);
		}
		logger.debug("insertTimeSheetTaskHdr   method end");
		return insertRes;
	}

	@Override
	public int insertTimeSheetTaskDtl(int responseHdrId, InsertTimeSheetTaskDtlRequest dtlObj) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO timesheet_tasks_dtl (TD_ID, DEFAULT_TASK_DTL, TENANT_ID) VALUES (?,?,?)";
			insertStatus = this.jdbcTemplate.update(qry, responseHdrId, dtlObj.getDefaultTaskDtl(),
					dtlObj.getTenantId());

		} catch (Exception ex) {
			logger.error("insertTimeSheetTaskDtl method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public int updateTimeSheetTask(UpdateTimeSheetTaskRequest updateTimeSheetTask) {
		int updateStatus = 0;
		int updateStatusHdr = 0;
		String tdId = "";
		try {
			String dtlQry = "update timesheet_tasks_dtl set DEFAULT_TASK_DTL=? where TD_DTL_ID=? and TENANT_ID=? ";
			updateStatus = this.jdbcTemplate.update(dtlQry,updateTimeSheetTask.getDefaultTaskDtl(),updateTimeSheetTask.getTdDtlId(),updateTimeSheetTask.getTenantId());
			if (updateStatus > 0) {

				String selectQry = "select TD_ID from timesheet_tasks_dtl  where TD_DTL_ID=? and TENANT_ID=?;";
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(selectQry,updateTimeSheetTask.getTdDtlId(),updateTimeSheetTask.getTenantId());
				tdId = resultMap.get("TD_ID").toString();


				String hdrQry = "update timesheet_tasks_hdr set UPDATED_BY=? , UPDATED_ON=NOW() where TD_ID=?";
				updateStatusHdr = this.jdbcTemplate.update(hdrQry,updateTimeSheetTask.getUpdatedBy(),tdId);

			}

		} catch (Exception ex) {
			logger.error("updateTimeSheetTask method Error" + ex);
		}
		return updateStatusHdr;
	}

	@Override
	public List<GetAllTimeSheetTaskForHdrEntity> getAllTimeSheetTaskForHdr(
			GetAllTimeSheetTaskForHdrRequest getTimeDtlByHdr) {
		List<GetAllTimeSheetTaskForHdrEntity> list = new ArrayList<>();
		try {
			String qry = " SELECT \r\n" + "    hdr.*,\r\n" + "    emupdated.EMPLOYEE_FIRSTNAME AS UPDATED_EMP_NAME,\r\n"
					+ "    emcreated.EMPLOYEE_FIRSTNAME AS CREATED_EMP_NAME\r\n" + "FROM\r\n"
					+ "    timesheet_tasks_hdr hdr,\r\n" + "    employee_mst emcreated,\r\n"
					+ "    employee_mst emupdated\r\n" + "WHERE\r\n" + "    hdr.CREATED_BY = emcreated.EMPLOYEE_ID\r\n"
					+ "        AND hdr.UPDATED_BY = emupdated.EMPLOYEE_ID\r\n" + "        AND hdr.TENANT_ID = '"
					+ getTimeDtlByHdr.getTenantId() + "'";

			RowMapper<GetAllTimeSheetTaskForHdrEntity> rowmapper = new GetAllTimeSheetTaskForHdrRowMapper();
			list = this.jdbcTemplate.query(qry, rowmapper);
		} catch (Exception ex) {
			logger.error("getAllTimeSheetTaskForHdr  method exception-->" + ex);
		}
		logger.debug("getAllTimeSheetTaskForHdr  method end");
		return list;
	}

	@Override
	public List<GetTimeSheetTaskForHdrandDtlEntity> getTimeSheetTaskForHdrandDtl(
			GetTimeSheetTaskForHdrandDtlRequset getTimeDtlByHdr) {
		List<GetTimeSheetTaskForHdrandDtlEntity> list = new ArrayList<>();
		try {
			String qry = "SELECT \r\n" + "    taskHdr.*,\r\n" + "    taskDtl.*,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS CREATED_EMP_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS UPDATED_EMP_NAME\r\n" + "FROM\r\n"
					+ "    timesheet_tasks_hdr taskHdr,\r\n" + "    timesheet_tasks_dtl taskDtl,\r\n"
					+ "    employee_mst em,\r\n" + "    employee_mst em1\r\n" + "WHERE\r\n"
					+ "    taskHdr.TD_ID = taskDtl.TD_ID\r\n" + "        AND taskHdr.CREATED_BY = em.EMPLOYEE_ID\r\n"
					+ "        AND taskHdr.UPDATED_BY = em1.EMPLOYEE_ID\r\n" + "        AND taskHdr.TD_ID = '"
					+ getTimeDtlByHdr.getTdId() + "'\r\n" + "        AND taskHdr.TENANT_ID = '"
					+ getTimeDtlByHdr.getTenantId() + "'";

			RowMapper<GetTimeSheetTaskForHdrandDtlEntity> rowmapper = new GetTimeSheetTaskForHdrandDtlRowMapper();
			list = this.jdbcTemplate.query(qry, rowmapper);
		} catch (Exception ex) {
			logger.error("getTimeSheetTaskForHdrandDtl  method exception-->" + ex);
		}
		logger.debug("getTimeSheetTaskForHdrandDtl  method end");
		return list;
	}

	@Override
	public int insertTimeSheetCategory(InsertTimeSheetCategoryRequest timeSheetCategory) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO timesheet_cateogry (TC_NAME, CREATED_BY, CREATED_ON, UPDATED_ON, UPDATED_BY, TENANT_ID) VALUES (?,?,NOW(),NOW(),?,?)";
			insertStatus = this.jdbcTemplate.update(qry, timeSheetCategory.getTcName(),
					timeSheetCategory.getCreatedBy(), timeSheetCategory.getUpdatedBy(),
					timeSheetCategory.getTenantId());

		} catch (Exception ex) {
			logger.error("insertTimeSheetTaskDtl method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public int updateTimeSheetCategory(UpdateTimeSheetCategoryRequest timeSheetCategory) {
		int updateStatus = 0;

		try {
			String dtlQry = "update timesheet_cateogry set TC_NAME=?,UPDATED_ON=NOW(),UPDATED_BY=? where TC_ID=? and TENANT_ID=? ";
			updateStatus = this.jdbcTemplate.update(dtlQry,timeSheetCategory.getTcName(),timeSheetCategory.getUpdatedBy(),timeSheetCategory.getTcId(),timeSheetCategory.getTenantId());

		} catch (Exception ex) {
			logger.error("updateTimeSheetCategory method Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public List<GetTimeSheetCategoryEntity> getTimeSheetCategory(GetTimeSheetCategoryRequest getTimeSheetCategory) {
		List<GetTimeSheetCategoryEntity> list = new ArrayList<>();
		try {
			String qry = " SELECT \r\n" + "    tc.*,\r\n" + "    em.EMPLOYEE_FIRSTNAME AS CREATED_EMP_NAME,\r\n"
					+ "    em1.EMPLOYEE_FIRSTNAME AS UPDATED_EMP_NAME\r\n" + "FROM\r\n"
					+ "    timesheet_cateogry tc,\r\n" + "    employee_mst em,\r\n" + "    employee_mst em1\r\n"
					+ "WHERE\r\n"
					+ " tc.UPDATED_BY=em1.EMPLOYEE_ID  and tc.CREATED_BY = em.EMPLOYEE_ID  and tc.TC_ID like ? and tc.TENANT_ID='"
					+ getTimeSheetCategory.getTenantId() + "'";

			RowMapper<GetTimeSheetCategoryEntity> rowmapper = new GetTimeSheetCategoryRowMapper();
			list = this.jdbcTemplate.query(qry, rowmapper, getTimeSheetCategory.getTcId());
		} catch (Exception ex) {
			logger.error("getTimeSheetCategory  method exception-->" + ex);
		}
		logger.debug("getTimeSheetCategory  method end");
		return list;
	}

	@Override
	public List<TimeSheetRequestsEntity> timeSheetReport(TimeSheetRequests timeSheetReport) {
	    List<TimeSheetRequestsEntity> list = new ArrayList<>();
	    String pmhdrid = timeSheetReport.getPmHdrId();

	    if (pmhdrid.equalsIgnoreCase("getall")) {
	        pmhdrid = "%%";
	    }

	    try {
	        String qry = "SELECT \r\n"
	                + "    ph.PROJECT_CODE, \r\n"
	                + "    ph.PROJECT_NAME, \r\n"
	                + "    taskHdr.RECORD_DATE, \r\n"
	                + "    taskDtl.TIMESHEET_DTL AS ACTIVITY, \r\n"
	                + "    em.EMPLOYEE_CODE, \r\n"
	                + "    em.EMPLOYEE_FIRSTNAME, \r\n"
	                + "    SUM(taskDtl.TIMESHEET_COST) AS RUPEES, \r\n"
	                + "    SUM(taskDtl.TIMESHEET_HRS) AS HOURS \r\n"
	                + "FROM \r\n"
	                + "    timesheet_hdr taskHdr \r\n"
	                + "    JOIN timesheet_dtl taskDtl ON taskHdr.T_HDR_ID = taskDtl.T_HDR_ID \r\n"
	                + "    JOIN project_hdr ph ON taskHdr.PM_HDR_ID = ph.PM_HDR_ID \r\n"
	                + "    JOIN employee_mst em ON taskHdr.EMPLOYEE_ID = em.EMPLOYEE_ID \r\n"
	                + "WHERE \r\n"
	                + "    taskHdr.PM_HDR_ID LIKE ? \r\n"
	                + "    AND taskHdr.RECORD_DATE BETWEEN ? AND ? \r\n"
	                + "    AND taskHdr.TENANT_ID = ? \r\n"
	                + "GROUP BY \r\n"
	                + "    ph.PROJECT_CODE, \r\n"
	                + "    taskHdr.RECORD_DATE, \r\n"
	                + "    em.EMPLOYEE_ID, \r\n"
	                + "    em.EMPLOYEE_FIRSTNAME \r\n"
	                + "ORDER BY \r\n"
	                + "    taskHdr.RECORD_DATE, \r\n"
	                + "    taskDtl.TIMESHEET_DTL, \r\n"
	                + "    em.EMPLOYEE_ID";

	        RowMapper<TimeSheetRequestsEntity> rowmapper = new TimeSheetRequestsRowMapper();
	        list = this.jdbcTemplate.query(qry, rowmapper, pmhdrid, 
	                timeSheetReport.getFromDate(), timeSheetReport.getToDate(), 
	                timeSheetReport.getTenantId());
	    } catch (Exception ex) {
	        logger.error("timeSheetReport method exception --> " + ex);
	    }

	    logger.debug("timeSheetReport method end");
	    return list;
	}

	@Override
	public List<TimeSheetMonthDtlEntity> timeSheetMonthDtl(TimeSheetRequests timeSheetReport) {
		List<TimeSheetMonthDtlEntity> list = new ArrayList<>();
		String pmhdrid = "";
		if (!timeSheetReport.getPmHdrId().isEmpty()) {
			pmhdrid = timeSheetReport.getPmHdrId();
		} else {
			pmhdrid = "%%";
		}
		try {
			String qry = "SELECT \r\n" + "    DATE_FORMAT(taskHdr.RECORD_DATE, '%m') AS MONTHS,\r\n"
					+ "    SUM(taskDtl.TIMESHEET_HRS) AS TOTAL_HOURS\r\n" + "FROM\r\n" + "    timesheet_hdr taskHdr\r\n"
					+ "JOIN\r\n" + "    timesheet_dtl taskDtl ON taskHdr.T_HDR_ID = taskDtl.T_HDR_ID\r\n" + "JOIN\r\n"
					+ "    project_hdr ph ON taskHdr.PM_HDR_ID = ph.PM_HDR_ID\r\n" + "JOIN\r\n"
					+ "    employee_mst em ON taskHdr.EMPLOYEE_ID = em.EMPLOYEE_ID\r\n" + " JOIN\r\n"
					+ "   department dept ON dept.DEPARTMENT_CODE = em.DEPARTMENT_CODE\r\n" + "WHERE\r\n"
					+ "    taskHdr.PM_HDR_ID like ?  \r\n" + "    AND taskHdr.RECORD_DATE BETWEEN '"
					+ timeSheetReport.getFromDate() + "' AND '" + timeSheetReport.getToDate() + "'  \r\n"
					+ "    AND taskHdr.TENANT_ID = '" + timeSheetReport.getTenantId() + "'  \r\n" + "GROUP BY\r\n"
					+ "    DATE_FORMAT(taskHdr.RECORD_DATE, '%m')\r\n" + "ORDER BY\r\n"
					+ "    DATE_FORMAT(taskHdr.RECORD_DATE, '%m')";

			RowMapper<TimeSheetMonthDtlEntity> rowmapper = new TimeSheetMonthDtlRowMapper();
			list = this.jdbcTemplate.query(qry, rowmapper, pmhdrid);
		} catch (Exception ex) {
			logger.error("timeSheetMonthDtl  method exception-->" + ex);
		}
		logger.debug("timeSheetMonthDtl  method end");
		return list;
	}

	@Override
	public List<TimeSheetDeptDtlEntity> timeSheetDeptDtl(TimeSheetRequests timeSheetReport) {
	    List<TimeSheetDeptDtlEntity> list = new ArrayList<>();

	    String pmhdrid = timeSheetReport.getPmHdrId();
	    if (pmhdrid.equalsIgnoreCase("getall")) {
	        pmhdrid = "%%";
	    }

	    try {
	        String qry = "SELECT \r\n"
	                   + "    dept.DEPARTMENT_CODE, \r\n"
	                   + "    dept.DEPARTMENT_NAME, \r\n"
	                   + "    SUM(taskDtl.TIMESHEET_HRS) AS HOURS, \r\n"
	                   + "    SUM(taskDtl.TIMESHEET_COST) AS RUPEES \r\n"
	                   + "FROM \r\n"
	                   + "    timesheet_hdr taskHdr, \r\n"
	                   + "    timesheet_dtl taskDtl, \r\n"
	                   + "    project_hdr ph, \r\n"
	                   + "    employee_mst em, \r\n"
	                   + "    department dept \r\n"
	                   + "WHERE \r\n"
	                   + "    taskHdr.T_HDR_ID = taskDtl.T_HDR_ID \r\n"
	                   + "    AND taskHdr.PM_HDR_ID = ph.PM_HDR_ID \r\n"
	                   + "    AND taskHdr.EMPLOYEE_ID = em.EMPLOYEE_ID \r\n"
	                   + "    AND em.DEPARTMENT_CODE = dept.DEPARTMENT_CODE \r\n"
	                   + "    AND taskHdr.PM_HDR_ID LIKE ? \r\n"  
	                   + "    AND taskHdr.RECORD_DATE BETWEEN ? AND ? \r\n"
	                   + "    AND taskHdr.TENANT_ID = ? \r\n"
	                   + "GROUP BY dept.DEPARTMENT_CODE, em.EMPLOYEE_ID";

	        RowMapper<TimeSheetDeptDtlEntity> rowmapper = new TimeSheetDeptDtlRowMapper();
	        list = this.jdbcTemplate.query(qry, rowmapper, pmhdrid, timeSheetReport.getFromDate(),
	                timeSheetReport.getToDate(), timeSheetReport.getTenantId());

	    } catch (Exception ex) {
	        logger.error("timeSheetDeptDtl method exception-->" + ex);
	    }
	    logger.debug("timeSheetDeptDtl method end");
	    return list;
	}

	@Override
	public int updateTimeSheetTaskHdrGroupName(UpdateTimeSheetTaskHdrGroupName updateTimeSheetTask) {
		int updateStatus = 0;
		try {
			String hdrQry = "update timesheet_tasks_hdr set TASK_GROUP_NAME=? , UPDATED_ON=NOW() where TD_ID=? and TENANT_ID=?";
			updateStatus = this.jdbcTemplate.update(hdrQry,updateTimeSheetTask.getTaskGroupName(),
					updateTimeSheetTask.getTdId(),updateTimeSheetTask.getTenantId());

		} catch (Exception ex) {
			logger.error("updateTimeSheetTaskHdrGroupName method Error" + ex);
		}
		return updateStatus;
	}

	@Override
	public List<TaskEntryHdrAndDtlEntity> taskEntryHdrAndDtl(TaskEntryHdrAndDtl timeSheetReport) {
		List<TaskEntryHdrAndDtlEntity> distictList = new ArrayList<>();
		List<TaskEntryHdrAndDtlEntity> allList = new ArrayList<>();
		try {

			String distinctqry = "SELECT DISTINCT\r\n" + "    dtl.TT_DTL_ID,\r\n" + "    dtl.ACTIVITY_NAME,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS ASSIGNED_TO,\r\n" + "    dtl.TE_DTl_ID,\r\n"
					+ "    hdr.TE_HDR_ID,\r\n" + "    tcm.TC_DESC,\r\n" + "    dtl.QTY,\r\n" + "    ttm.TT_DESC\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr,\r\n" + "    task_entry_dtl dtl,\r\n"
					+ "    task_type_mst ttm,\r\n" + "    task_category_mst tcm,\r\n" + "    employee_mst em\r\n"
					+ "WHERE\r\n" + "    hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n"
					+ "        AND hdr.TASK_TYPE_CODE = ttm.TT_CODE\r\n"
					+ "        AND hdr.TASK_CATEGORY_CODE = tcm.TC_CODE and em.EMPLOYEE_ID = dtl.ASSIGNED_TO  \r\n"
					+ "        AND hdr.PM_HDR_ID = '" + timeSheetReport.getPmHdrId() + "' and hdr.TENANT_ID='"
					+ timeSheetReport.getTenantId() + "'" + "        and tcm.TC_CODE='"
					+ timeSheetReport.getCategoryCode() + "' and ttm.TT_CODE='" + timeSheetReport.getTypeCode()
					+ "' \r\n" + "        AND dtl.TT_DTL_ID != 0";

			RowMapper<TaskEntryHdrAndDtlEntity> rowmapper = new TaskEntryHdrAndDtlRowMapper();
			distictList = this.jdbcTemplate.query(distinctqry, rowmapper);

			String allqry = "SELECT \r\n" + "    dtl.TT_DTL_ID,\r\n" + "    dtl.ACTIVITY_NAME,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS ASSIGNED_TO,\r\n" + "    dtl.TE_DTl_ID,\r\n"
					+ "    hdr.TE_HDR_ID,\r\n" + "    tcm.TC_DESC,\r\n" + "    dtl.QTY,\r\n" + "    ttm.TT_DESC\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr,\r\n" + "    task_entry_dtl dtl,\r\n"
					+ "    task_type_mst ttm,\r\n" + "    task_category_mst tcm,\r\n" + "    employee_mst em\r\n"
					+ "WHERE\r\n" + "    hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n"
					+ "        AND hdr.TASK_TYPE_CODE = ttm.TT_CODE\r\n"
					+ "        AND hdr.TASK_CATEGORY_CODE = tcm.TC_CODE and em.EMPLOYEE_ID = dtl.ASSIGNED_TO \r\n"
					+ "        AND hdr.PM_HDR_ID = '" + timeSheetReport.getPmHdrId() + "' and hdr.TENANT_ID='"
					+ timeSheetReport.getTenantId() + "' \r\n" + "        and tcm.TC_CODE='"
					+ timeSheetReport.getCategoryCode() + "' and ttm.TT_CODE='" + timeSheetReport.getTypeCode()
					+ "' \r\n" + "        AND dtl.TT_DTL_ID = 0";

			RowMapper<TaskEntryHdrAndDtlEntity> rowmapper1 = new TaskEntryHdrAndDtlRowMapper();
			allList = this.jdbcTemplate.query(allqry, rowmapper1);
			allList.addAll(distictList);

		} catch (Exception ex) {
			logger.error("taskEntryHdrAndDtl  method exception-->" + ex);
		}
		logger.debug("taskEntryHdrAndDtl  method end");
		return allList;
	}

	@Override
	public int deleteTimeSheetTaskDtl(DeleteTimeSheetTaskDtlRequest deletereq) {
		int deleteDtl = 0;
		try {
			String dtlQry = "delete from timesheet_tasks_dtl where TD_DTL_ID=? and  TENANT_ID=?";
			deleteDtl = this.jdbcTemplate.update(dtlQry,deletereq.getTdDtlId(),deletereq.getTenantId());
		} catch (Exception ex) {
			logger.error("deleteTimeSheetTaskDtl method Error" + ex);
		}
		return deleteDtl;
	}

	@Override
	public int CheckTDIdTaskDtl(DeleteTimeSheetTaskDtlRequest deletereq) {
		int ChcktdId = 0;
		int deleteTDRec = 0;
		try {
			String ChckQuery = "SELECT COUNT(TD_ID) > 0  as COUNT\r\n" + "    FROM timesheet_tasks_dtl\r\n"
					+ "    WHERE TD_ID =? AND TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChckQuery,deletereq.getTdId(),deletereq.getTenantId());
			ChcktdId = Integer.parseInt(resultMap.get("COUNT").toString());
			if (ChcktdId == 0) {
				String delQry = "DELETE FROM timesheet_tasks_hdr WHERE TD_ID = ? AND TENANT_ID = ?;";
				deleteTDRec = this.jdbcTemplate.update(delQry,deletereq.getTdId(),deletereq.getTenantId());
			}

		} catch (Exception ex) {
			logger.error("CheckTDIdTaskDtl method Error" + ex);
		}
		return deleteTDRec;
	}

	@Override
	public int insertIndividualTimeSheetTaskDtl(InsertIndividualTimeSheetTaskDtl insertTimeSheet) {
		int insertStatus = 0;
		try {
			String qry = "INSERT INTO timesheet_tasks_dtl (TD_ID, DEFAULT_TASK_DTL, TENANT_ID) VALUES (?,?,?)";
			insertStatus = this.jdbcTemplate.update(qry, insertTimeSheet.getTdId(), insertTimeSheet.getDefaultTaskDtl(),
					insertTimeSheet.getTenantId());

		} catch (Exception ex) {
			logger.error("insertIndividualTimeSheetTaskDtl method Error" + ex);
		}
		return insertStatus;
	}

	@Override
	public String getEmployeDeptAssigned(String empDepCode, String tenantId) {
		String deptAssign = "";
		try {
			String deptCheckQ = "select count(*) as COUNT from project_wbs_initiation_mst where DEPARTMENT_CODE=? and TENANT_ID=?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(deptCheckQ,empDepCode,tenantId);
			int deptCount = Integer.parseInt(resultMap.get("COUNT").toString());

			if (deptCount > 0) {

				String qrySelect = "select DEPARTMENT_ASSIGNED AS DEPARTMENT_ASSIGNED from project_wbs_initiation_mst where DEPARTMENT_CODE=? and TENANT_ID=?;";
				Map<String, Object> resultMap1 = jdbcTemplate.queryForMap(qrySelect,empDepCode,tenantId);
				deptAssign = resultMap1.get("DEPARTMENT_ASSIGNED").toString();

			}
		} catch (Exception ex) {
			logger.error("getEmployeDeptAssigned method Error" + ex);
		}
		return deptAssign;
	}

	@Override
	public int deleteDtlActivityforEmpId(DeleteActivityforEmpIdRequest deletereq) {
		int deleteDtl = 0;
		try {

			String dtlQry = "delete from timesheet_dtl where T_DTL_ID=? and  TENANT_ID=?";
			deleteDtl = this.jdbcTemplate.update(dtlQry,deletereq.getTdtlId(),deletereq.getTenantId());

		} catch (Exception ex) {
			logger.error("deleteDtlActivityforEmpId method Error" + ex);
		}
		return deleteDtl;
	}

	@Override
	public BigDecimal getSalaryFromId(String empId) {
		BigDecimal appSal = BigDecimal.ZERO;
		try {

			String dtlQry = "select APPROX_SALARY from employee_mst where EMPLOYEE_ID= ?";
			Object[] args = { empId };
			appSal = this.jdbcTemplate.queryForObject(dtlQry, BigDecimal.class, args);

		} catch (Exception ex) {
			logger.error("getSalaryFromId method Error" + ex);
		}
		return appSal;
	}

	@Override
	public String getPropValueByTenant(String tenantId, String propertyName) {
		String propertyValue = "";
		try {
			String value = "select PROPERTY_VALUE from tenant_property_mst where TENANT_ID = ? and PROPERTY_NAME = ? and IS_ACTIVE = '1'";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(value,tenantId,propertyName);
			String propValue  = resultMap.get("PROPERTY_VALUE").toString();
			propertyValue = (propValue != null ? propValue : "");
		} catch (Exception e) {
			logger.error("getPropValueByTenant Method Exception " + e);
		}
		return propertyValue;
	}

	@Override
	public List<TimeSheetRequestsEntity> timeSheetReportCategory(String fromDate, String toDate, String pmHdrId,
			String tenantId, String category) {
		List<TimeSheetRequestsEntity> list = new ArrayList<>();	
	    String categories = "'" + category.replace(",", "','") + "'";
		if (pmHdrId.isEmpty()) {
			pmHdrId = "like '%%'";
		}
		
		try {
			String baseQry = "SELECT\r\n" + 
					"    ph.PROJECT_CODE,\r\n" + 
					"    ph.PROJECT_NAME,\r\n" + 
					"    taskHdr.RECORD_DATE, taskHdr.SUMMARY,\r\n" + 
					"    taskDtl.TIMESHEET_DTL AS ACTIVITY,\r\n" + 
					"    em.EMPLOYEE_CODE,\r\n" + 
					"    em.EMPLOYEE_FIRSTNAME,\r\n" + 
					"    SUM(taskDtl.TIMESHEET_COST) AS RUPEES,\r\n" + 
					"    SUM(taskDtl.TIMESHEET_HRS) AS HOURS\r\n" + 
					"FROM\r\n" + 
					"    timesheet_hdr taskHdr,\r\n" + 
					"    timesheet_dtl taskDtl,\r\n" + 
					"    project_hdr ph,\r\n" + 
					"    employee_mst em,\r\n" + 
					"    timesheet_cateogry tc\r\n" + 
					"WHERE\r\n" + 
					"    tc.TC_ID = taskHdr.TC_ID \r\n" + 
					"    AND taskHdr.EMPLOYEE_ID = em.EMPLOYEE_ID\r\n" + 
					"    AND taskHdr.PM_HDR_ID = ph.PM_HDR_ID\r\n" + 
					"    AND taskHdr.T_HDR_ID = taskDtl.T_HDR_ID\r\n" + 
					"    AND taskHdr.PM_HDR_ID = ?\r\n" + 
					"    AND taskHdr.TENANT_ID = ?\r\n" + 
					"    AND tc.TC_ID in (%s)\r\n" + 
					"	AND taskHdr.RECORD_DATE BETWEEN ? and ?\r\n" + 
					"GROUP BY\r\n" + 
					"    ph.PROJECT_CODE,\r\n" + 
					"    taskHdr.RECORD_DATE,\r\n" + 
					"    em.EMPLOYEE_ID,\r\n" + 
					"    em.EMPLOYEE_FIRSTNAME\r\n" + 
					"ORDER BY\r\n" + 
					"    taskHdr.RECORD_DATE,\r\n" + 
					"    taskDtl.TIMESHEET_DTL,\r\n" + 
					"    em.EMPLOYEE_ID;";

			 String qry = String.format(baseQry, categories);
			 
			RowMapper<TimeSheetRequestsEntity> rowmapper = new TimeSheetRequestsRowMapper();
			list = this.jdbcTemplate.query(qry, rowmapper, pmHdrId, tenantId, fromDate, toDate);
		} catch (Exception ex) {
			logger.error("timeSheetReportCategory  method exception-->" + ex);
		}
		logger.debug("timeSheetReportCategory  method end");
		return list;
	}

	@Override
	public String timelogremaining(TimeSheetRemainingLogRequest timelogReq) {
		// TODO Auto-generated method stub
		String empId = timelogReq.getEmpId();
		String date = timelogReq.getDate();
		String tenantID = timelogReq.getTenantId();
		BigDecimal result = BigDecimal.ZERO;
		String remaining = "";
		try {
			String logDays = GetPropertyValue.getPropValue("LOG_HOURS", tenantID, this.jdbcTemplate);
			String qry = "SELECT COALESCE(SUM(dtl.TIMESHEET_HRS), 0) \r\n" + 
					"FROM timesheet_dtl dtl \r\n" + 
					"INNER JOIN timesheet_hdr hdr \r\n" + 
					"ON dtl.T_HDR_ID = hdr.T_HDR_ID \r\n" + 
					"WHERE hdr.RECORD_DATE = '"+date+"' \r\n" + 
					"AND hdr.EMPLOYEE_ID = '"+empId+"' AND hdr.TENANT_ID='"+tenantID+"'";
			String sum = this.jdbcTemplate.queryForObject(qry, String.class);
			if(!sum.equalsIgnoreCase("0.00")) {
				result = new BigDecimal(logDays).subtract(new BigDecimal(sum));
			}else {
				result = new BigDecimal(logDays);
			}
			remaining = String.valueOf(result);			
			
		}catch (Exception e) {
			logger.error("timelogremaining  method exception-->" + e);			
		}
		return remaining ;
	}
}
