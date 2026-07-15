package com.vmfg.mis.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.mis.dao.interfaces.IAssemblyMisDAO;
import com.vmfg.mis.entity.DesignWidgetDtlEntity;
import com.vmfg.mis.entity.ProjectProgressEntity;
import com.vmfg.mis.entity.TaskCompTimeEntity;
import com.vmfg.mis.entity.getAssyDtlTaskReportEntity;
import com.vmfg.mis.entity.getAssyTaskReportEntity;
import com.vmfg.mis.entity.getPojCompDtlEntity;
import com.vmfg.mis.rowmapper.AssyDtlTaskReportRowMapper;
import com.vmfg.mis.rowmapper.AssyTaskReportRowMapper;
import com.vmfg.mis.rowmapper.DesignWidgetDtlMisRowMapper;
import com.vmfg.mis.rowmapper.ProjCompDtlRowMapper;
import com.vmfg.mis.rowmapper.ProjectProgressRowMapper;
import com.vmfg.mis.rowmapper.TaskCompTimeRowMapper;


@Transactional
@Repository
public class AssemblyMisDAO implements IAssemblyMisDAO {
	private static final Logger logger = LoggerFactory.getLogger(AssemblyMisDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public int assyWidgetDtlDAO(String tenantId, String empId,
			String deptCode, String pmId, String projId) {
		int Qry = 0;
		try {	
			String ChkQry ="SELECT \r\n" + 
					"     CASE \r\n" + 
					"        WHEN COUNT(hdr.PM_HDR_ID) = 0 THEN '0'\r\n" + 
					"        ELSE CAST(COUNT(hdr.PM_HDR_ID) AS CHAR)\r\n" + 
					"    END AS PM_HDR_ID\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.ASSY_HDR_ID = pteam.MASTER_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = ?\r\n" + 
					"        AND hdr.IS_COMPLETED = '0' \r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = ?\r\n" + 
					"        AND PM_ID = ?\r\n" + 
					"        AND pteam.IS_ACTIVE = 1 " ;		 
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChkQry, tenantId, empId, pmId);
			Qry = Integer.parseInt(resultMap.get("PM_HDR_ID").toString());
			
		}catch(Exception ex) {
			logger.error("assyWidgetDtlDAO Error" + ex);	
		}
		return Qry;
	}

	@Override
	public List<TaskCompTimeEntity> getTaskCompTimeDAO(String monthYr, String tenantId, String empId, 
			String deptCode, String pmId, String projId) {
		List<TaskCompTimeEntity> list = new ArrayList<TaskCompTimeEntity>();
	
		try {	
//			String month=monthYr.split("-")[1];
//			String year=monthYr.split("-")[0];
			String query ="SELECT \r\n" + 
					"    tdtl.ACTIVITY_NAME, tdtl.COMPLETED_PTG, phdr.PROJECT_NAME, \r\n" + 
					"    tdtl.PLANNED_START_DATE, tdtl.PLANNED_COMPLETED_DATE,\r\n" + 
					"    tdtl.ACTUAL__START_DATE, tdtl.COMPLETED_DATE,\r\n" +
					"   CASE\r\n" + 
					"        WHEN DATE(tdtl.COMPLETED_DATE) < tdtl.PLANNED_COMPLETED_DATE THEN 0\r\n" + 
					"        ELSE DATEDIFF(DATE(tdtl.COMPLETED_DATE), tdtl.PLANNED_COMPLETED_DATE)\r\n" + 
					"    END AS DELAY\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"		INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID =  tdtl.TE_HDR_ID\r\n" +
					"   INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.ASSY_HDR_ID = pteam.MASTER_ID\r\n " +
					"	INNER JOIN \r\n" + 
					"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = '"+tenantId+"' AND hdr.PM_HDR_ID LIKE '"+projId+"'\r\n" + 
					"        AND tdtl.IS_COMPLETED = '1'\r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
				    "        AND tdtl.COMPLETED_DATE IS NOT NULL\r\n" +
					"        AND thdr.DEPARTMENT_CODE = '"+deptCode+"'\r\n" + 
					"        AND PM_ID = '"+pmId+"' ";
//				    "       and month(tdtl.DUE_DATE)='"+month+"'and year(tdtl.DUE_DATE)='"+year+"' " ; 
					
			list = this.jdbcTemplate.query(query, new TaskCompTimeRowMapper());
		}catch(Exception ex) {
			logger.error("getTaskCompTimeDAO Error" + ex);	
		}
		return list;
	}

	@Override
	public List<TaskCompTimeEntity> getTaskCompTimeDAO1(String monthYr, String tENANTID, String empId, String deptCode,
			String pmId, String projectId) {
		List<TaskCompTimeEntity> list = new ArrayList<TaskCompTimeEntity>();
		int cnt = 0;
		try {
			String ChkQry ="select count(PM_HDR_ID) as PM_HDR_ID from project_hdr where PM_HDR_ID like ?"
				     + "     AND TENANT_ID = ?;";
		
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChkQry, projectId, tENANTID);
			cnt = Integer.parseInt(resultMap.get("PM_HDR_ID").toString());

		if(cnt > 0) {
			String query ="SELECT \r\n" + 
					"	tdtl.ACTIVITY_NAME, tdtl.COMPLETED_PTG, phdr.PROJECT_NAME, \r\n" + 
					"    tdtl.PLANNED_START_DATE, tdtl.PLANNED_COMPLETED_DATE, tdtl.TE_DTl_ID,\r\n" + 
					"   CASE\r\n" + 
					"        WHEN tdtl.PLANNED_COMPLETED_DATE < tdtl.PLANNED_START_DATE THEN 0\r\n" + 
					"        ELSE DATEDIFF(tdtl.PLANNED_COMPLETED_DATE, tdtl.PLANNED_START_DATE)\r\n" + 
					"    END AS DELAY\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"		INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID =  tdtl.TE_HDR_ID\r\n" + 
					"	INNER JOIN \r\n" + 
					"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = '"+tENANTID+"' AND hdr.PM_HDR_ID in (SELECT  \r\n" + 
					"					            assy.PM_HDR_ID \r\n" + 
					"					        FROM \r\n" + 
					"					            process_assigned_team pat inner join assy_hdr assy\r\n" + 
					"                                ON MASTER_ID = assy.ASSY_HDR_ID\r\n" + 
					"					        WHERE \r\n" + 
					"					            pat.PM_ID = '"+pmId+"' AND pat.IS_ACTIVE = 1 \r\n" + 
					"					                AND pat.ASSIGNED_EMP_ID = '"+empId+"')\r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND hdr.COMPLETED_DATETIME IS NOT NULL\r\n" + 
					"        AND thdr.DEPARTMENT_CODE = '"+deptCode+"'\r\n" + 
					"		AND hdr.IS_COMPLETED = '0'\r\n" + 
					"        AND tdtl.DUE_DATE BETWEEN DATE_FORMAT(STR_TO_DATE('"+monthYr+"', '%Y-%m'),\r\n" +
					"            '%Y-%m-01') AND LAST_DAY(STR_TO_DATE('"+monthYr+"', '%Y-%m'));";
			list = this.jdbcTemplate.query(query, new TaskCompTimeRowMapper());
		}
		}catch(Exception ex) {
			logger.error("getTaskCompTimeDAO1 Error" + ex);	
		}
		return list;
	}

	@Override
	public List<getPojCompDtlEntity> getPojCompDtlDAO(String tenantId, String empId, String deptCode,
			String pmId, String projId) {
		List<getPojCompDtlEntity> list = new ArrayList<getPojCompDtlEntity>();
		try {	
//			String month=monthYr.split("-")[1];
//			String year=monthYr.split("-")[0];
			String query ="	SELECT \r\n" + 
					"    phdr.PROJECT_NAME,\r\n" + 
					"    phdr.PROJECT_CODE,\r\n" + 
					"    tdtl.ACTIVITY_NAME as ACTIVITY_NAME ,\r\n" + 
					"    tdtl.PLANNED_START_DATE as PLANNED_START_DATE ,\r\n" + 
					"    tdtl.COMPLETED_DATE as COMPLETED_DATE ,\r\n" + 
					"    tdtl.PLANNED_COMPLETED_DATE as PLANNED_COMPLETED_DATE,\r\n" + 
					"    phdr.CUSTOMER_NAME as CUSTOMER_NAME,\r\n" + 
					"    tdtl.ACTUAL__START_DATE as ACTUAL__START_DATE,\r\n" + 
					"    CASE\r\n" + 
					"        WHEN DATE(tdtl.COMPLETED_DATE) < tdtl.PLANNED_COMPLETED_DATE THEN 0\r\n" + 
					"        ELSE DATEDIFF(DATE(tdtl.COMPLETED_DATE),\r\n" + 
					"                tdtl.PLANNED_COMPLETED_DATE)\r\n" + 
					"    END AS DELAY\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID = tdtl.TE_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.ASSY_HDR_ID = pteam.MASTER_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND hdr.PM_HDR_ID LIKE '"+projId+"'\r\n" + 
					"        AND hdr.IS_COMPLETED = '1'\r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
					"        AND pteam.IS_ACTIVE = 1\r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND hdr.COMPLETED_DATETIME IS NOT NULL\r\n" + 
					"        AND thdr.DEPARTMENT_CODE = '"+deptCode+"'\r\n" + 
					"        AND PM_ID = '"+pmId+"';";

			list = this.jdbcTemplate.query(query, new ProjCompDtlRowMapper());
		}catch(Exception ex) {
			logger.error("getPojCompDtlDAO Error" + ex);	
		}
		return list;
	}

	@Override
	public List<getPojCompDtlEntity> getPojCompDtlDAO1(String monthYr, String tENANTID, String empId, String deptCode,
			String pmId, String projectId) {
		List<getPojCompDtlEntity> list = new ArrayList<getPojCompDtlEntity>();
		int cnt = 0;
		try {
			String ChkQry ="select count(PM_HDR_ID) as PM_HDR_ID from project_hdr where PM_HDR_ID like ?"
				     + "     AND TENANT_ID = ?;";
		
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChkQry, projectId, tENANTID);
			cnt = Integer.parseInt(resultMap.get("PM_HDR_ID").toString());
		
		if(cnt > 0) {
			String query ="SELECT \r\n" + 
					"    phdr.PROJECT_NAME,tdtl.ACTIVITY_NAME,\r\n" + 
					"    hdr.PLANNED_START_DATE,\r\n" + 
					"    hdr.PLANNED_END_DATE\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"		INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID =  tdtl.TE_HDR_ID\r\n" + 
					"	INNER JOIN \r\n" + 
					"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
				    "    hdr.TENANT_ID = '"+tENANTID+"' AND hdr.PM_HDR_ID in (SELECT  \r\n" + 
				    "					            assy.PM_HDR_ID \r\n" + 
					"					        FROM \r\n" + 
					"					            process_assigned_team pat INNER JOIN assy_hdr assy\r\n" + 						"                                ON MASTER_ID = assy.ASSY_HDR_ID\r\n" + 
					"					        WHERE \r\n" + 
					"					            PM_ID = '"+pmId+"' AND IS_ACTIVE = 1 \r\n" + 
					"					                AND ASSIGNED_EMP_ID = '"+empId+"')\r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND hdr.COMPLETED_DATETIME IS NOT NULL\r\n" + 
					"        AND thdr.DEPARTMENT_CODE = '"+deptCode+"'\r\n" + 
					"		AND hdr.IS_COMPLETED = '1'\r\n" + 
					"        AND tdtl.DUE_DATE BETWEEN DATE_FORMAT(STR_TO_DATE('"+monthYr+"', '%Y-%m'),\r\n" + 
					"            '%Y-%m-01') AND LAST_DAY(STR_TO_DATE('"+monthYr+"', '%Y-%m'));";
			list = this.jdbcTemplate.query(query, new ProjCompDtlRowMapper());
		  }
		}catch(Exception ex) {
			logger.error("getTaskCompTimeDAO1 Error" + ex);	
		}
		return list;
	}

	@Override
	public List<getAssyTaskReportEntity> getAssyTaskReportDAO(String monthYr, String tenantId, String empId,
			String deptCode, String pmId, String projId, String lifespan) {
		List<getAssyTaskReportEntity> list = new ArrayList<getAssyTaskReportEntity>();
		
		try {
			String month=monthYr.split("-")[1];
			String year=monthYr.split("-")[0];
			String monthYear = "";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and month(tdtl.DUE_DATE)='"+month+"'and year(tdtl.DUE_DATE)='"+year+"'";
			}
			String query ="SELECT \r\n" + 
					"    phdr.PM_HDR_ID, phdr.PROJECT_CODE,\r\n" + 
					"    COUNT(CASE\r\n" + 
					"        WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\r\n" + 
					"    END) AS OPEN_TASK,\r\n" + 
					"    COUNT(tdtl.COMPLETED_DATE IS NULL OR NULL) AS PENDING_TASK,\r\n" + 
					"    SUM(CASE\r\n" + 
					"        WHEN tdtl.COMPLETED_DATE IS NOT NULL THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END) AS COMPLETED_TASK,AVG(tdtl.COMPLETED_PTG) AS COMPLETED_PERCENTAGE\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID = tdtl.TE_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.ASSY_HDR_ID = pteam.MASTER_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND hdr.PM_HDR_ID LIKE '"+projId+"'\r\n" + 
					"        AND hdr.IS_COMPLETED = '0'\r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
					"        AND pteam.IS_ACTIVE = 1\r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND thdr.DEPARTMENT_CODE = '"+deptCode+"'\r\n" + 
					"        AND PM_ID = '"+pmId+"' "+monthYear+" GROUP BY phdr.PM_HDR_ID; " ;
			//		"        AND tdtl.DUE_DATE BETWEEN DATE_FORMAT(STR_TO_DATE('"+monthYr+"', '%Y-%m'),\r\n" + 
			//		"            '%Y-%m-01') AND LAST_DAY(STR_TO_DATE('"+monthYr+"', '%Y-%m'))\r\n" + 
				
			list = this.jdbcTemplate.query(query, new AssyTaskReportRowMapper());
		}catch(Exception ex) {
			logger.error("getAssyTaskReportDAO Error" + ex);	
		}
		return list;
	}

	@Override
	public List<getAssyTaskReportEntity> getAssyTaskReportDAO1(String monthYr, String tENANTID, String empId,
			String deptCode, String pmId, String projectId, String lifespan) {
		List<getAssyTaskReportEntity> list = new ArrayList<getAssyTaskReportEntity>();
		int cnt = 0;
		try {
			String ChkQry ="select count(PM_HDR_ID) AS PM_HDR_ID from project_hdr where PM_HDR_ID like ? "
				     + "     AND TENANT_ID = ?;";
		
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChkQry, projectId, tENANTID);
			cnt = Integer.parseInt(resultMap.get("PM_HDR_ID").toString());
			String monthYear = "";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "AND tdtl.DUE_DATE BETWEEN DATE_FORMAT(STR_TO_DATE('" + monthYr + "', '%Y-%m'), " +
						"'%Y-%m-01') AND LAST_DAY(STR_TO_DATE('" + monthYr + "', '%Y-%m'))";
			}
		if(cnt > 0) {
			String query ="SELECT \r\n" + 
					"    phdr.PM_HDR_ID, phdr.PROJECT_CODE,\r\n" + 
					"    COUNT(CASE\r\n" + 
					"        WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\r\n" + 
					"    END) AS OPEN_TASK,\r\n" + 
					"    COUNT(tdtl.COMPLETED_DATE IS NULL OR NULL) AS PENDING_TASK,\r\n" + 
					"    SUM(CASE\r\n" + 
					"        WHEN tdtl.COMPLETED_DATE IS NOT NULL THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END) AS COMPLETED_TASK,AVG(tdtl.COMPLETED_PTG) AS COMPLETED_PERCENTAGE\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID = tdtl.TE_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = '"+tENANTID+"'\r\n" + 
					"        AND hdr.PM_HDR_ID IN (SELECT  \r\n" + 
					"					            assy.PM_HDR_ID \r\n" + 
					"					        FROM \r\n" + 
					"					            process_assigned_team pat inner join assy_hdr assy\r\n" + 
					"                                ON MASTER_ID = assy.ASSY_HDR_ID \r\n" + 
					"					        WHERE \r\n" + 
					"					            PM_ID = '"+pmId+"' AND IS_ACTIVE = 1 \r\n" + 
					"					                AND ASSIGNED_EMP_ID = '"+empId+"')\r\n" + 
					"        AND hdr.IS_COMPLETED = '0'                            \r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND thdr.DEPARTMENT_CODE = '"+deptCode+"'\r\n" + 
					"       '"+monthYear+"'\r\n" +
					"GROUP BY phdr.PM_HDR_ID;";
			list = this.jdbcTemplate.query(query, new AssyTaskReportRowMapper());
		  }
		}catch(Exception ex) {
			logger.error("getAssyTaskReportDAO1 Error" + ex);	
		}
		return list;
	}

	@Override
	public List<getAssyDtlTaskReportEntity> getAssyDtlTaskReportDAO(String monthYr, String tENANTID, String empId,
			String deptCode, String pmId, String projectId, String lifespan) {
		List<getAssyDtlTaskReportEntity> list = new ArrayList<getAssyDtlTaskReportEntity>();
		int cnt = 0;
		try {
			String month=monthYr.split("-")[1];
			String year=monthYr.split("-")[0];
			String monthYear = "";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and month(tdtl.DUE_DATE)='"+month+"'and year(tdtl.DUE_DATE)='"+year+"'";
			}
			String ChkQry ="select count(PM_HDR_ID) AS PM_HDR_ID from project_hdr where PM_HDR_ID like ?"
				     + "     AND TENANT_ID = ?;";
		
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChkQry, projectId, tENANTID);
			cnt = Integer.parseInt(resultMap.get("PM_HDR_ID").toString());
		
		if(cnt > 0) {
			String query ="select \r\n" + 
					" phdr.PM_HDR_ID, phdr.PROJECT_CODE, \r\n" + 
					" COUNT(CASE\r\n" + 
					"        WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\r\n" + 
					"    END) AS OPEN_TASK,\r\n" + 
					" COUNT(tdtl.COMPLETED_DATE IS NULL OR NULL) AS PENDING_TASK,\r\n" + 
					" SUM(CASE\r\n" + 
					"        WHEN tdtl.COMPLETED_DATE > tdtl.PLANNED_COMPLETED_DATE  \r\n" + 
//					"        tdtl.PLANNED_START_DATE < tdtl.COMPLETED_DATE\r\n" + 
					"        OR tdtl.COMPLETED_DATE IS NULL OR NULL  THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END) AS DELAY_TASK,\r\n" + 
					"    AVG(tdtl.COMPLETED_PTG) AS COMPLETED_PERCENTAGE\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID = tdtl.TE_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.ASSY_HDR_ID = pteam.MASTER_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"     hdr.TENANT_ID = '"+tENANTID+"'\r\n" + 
					"        AND hdr.PM_HDR_ID LIKE '"+projectId+"'\r\n" + 
					"        AND hdr.IS_COMPLETED = '0'\r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
					"        AND pteam.IS_ACTIVE = 1\r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND thdr.DEPARTMENT_CODE = '"+deptCode+"'\r\n" + 
					"        AND PM_ID = '"+pmId+"'  "+monthYear+" \r\n" +
					"GROUP BY phdr.PM_HDR_ID;";
			list = this.jdbcTemplate.query(query, new AssyDtlTaskReportRowMapper());
		  }
		}catch(Exception ex) {
			logger.error("getAssyDtlTaskReportDAO Error" + ex);	
		}
		return list;
	}

	@Override
	public List<getAssyDtlTaskReportEntity> getAssyDtlTaskReportDAO1(String monthYr, String tENANTID, String empId,
			String deptCode, String pmId, String projectId, String lifeSpan) {
		List<getAssyDtlTaskReportEntity> list = new ArrayList<getAssyDtlTaskReportEntity>();
		int cnt = 0;
		try {
			String ChkQry ="select count(PM_HDR_ID) as PM_HDR_ID from project_hdr where PM_HDR_ID like ?"
				     + "     AND TENANT_ID = ?;";
		
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChkQry, projectId, tENANTID);
			cnt = Integer.parseInt(resultMap.get("PM_HDR_ID").toString());
		String monthYear = "";
		if(lifeSpan.equalsIgnoreCase("0")) {
			monthYear = "AND tdtl.DUE_DATE BETWEEN DATE_FORMAT(STR_TO_DATE('" + monthYr + "', '%Y-%m'), " +
					"'%Y-%m-01') AND LAST_DAY(STR_TO_DATE('" + monthYr + "', '%Y-%m'))";
		}
		if(cnt > 0) {
			String query ="select \r\n" + 
					" phdr.PM_HDR_ID, phdr.PROJECT_CODE, \r\n" + 
					" COUNT(CASE\r\n" + 
					"        WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\r\n" + 
					"    END) AS OPEN_TASK,\r\n" + 
					" COUNT(tdtl.COMPLETED_DATE IS NULL OR NULL) AS PENDING_TASK,\r\n" + 
					" SUM(CASE\r\n" + 
					"        WHEN tdtl.PLANNED_COMPLETED_DATE < tdtl.COMPLETED_DATE AND \r\n" + 
					"        tdtl.PLANNED_START_DATE < tdtl.COMPLETED_DATE\r\n" + 
					"        OR tdtl.COMPLETED_DATE IS NULL OR NULL  THEN 1\r\n" + 
					"        ELSE 0\r\n" + 
					"    END) AS DELAY_TASK,\r\n" + 
					"    AVG(tdtl.COMPLETED_PTG) AS COMPLETED_PERCENTAGE\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID = tdtl.TE_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr phdr ON hdr.PM_HDR_ID = phdr.PM_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"     hdr.TENANT_ID = '"+tENANTID+"'\r\n" + 
					"        AND hdr.PM_HDR_ID IN (SELECT  \r\n" + 
					"					            assy.PM_HDR_ID \r\n" + 
					"					        FROM \r\n" + 
					"					            process_assigned_team pat inner join assy_hdr assy\r\n" + 
					"                                ON MASTER_ID = assy.ASSY_HDR_ID \r\n" + 
					"					        WHERE \r\n" + 
					"					            PM_ID = '"+pmId+"' AND IS_ACTIVE = 1 \r\n" + 
					"					                AND ASSIGNED_EMP_ID = '"+empId+"')\r\n" + 
					"        AND hdr.IS_COMPLETED = '0'\r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND thdr.DEPARTMENT_CODE = '"+deptCode+"'\r\n" + 
					" 		'" + monthYear + "';";
			list = this.jdbcTemplate.query(query, new AssyDtlTaskReportRowMapper());
		  }
		}catch(Exception ex) {
			logger.error("getAssyDtlTaskReportDAO1 Error" + ex);	
		}
		return list;
	}

	@Override
	public List<ProjectProgressEntity> getprojectInProgress(String tENANTID, String empId,
			String deptCode, String pmId, String projectId) {
		List<ProjectProgressEntity> list = new ArrayList<ProjectProgressEntity>();

		try {
			String dataQry="SELECT \r\n" + 
					"    hdr.PROJECT_NAME,\r\n" + 
					"    hdr.PLANNED_START_DATE,\r\n" + 
					"    hdr.PLANNED_END_DATE,\r\n" + 
					"    hdr.COMPLETED_DATETIME,\r\n" + 
					"    pr.PROJECT_CODE, pr.CUSTOMER_NAME\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    project_hdr pr ON pr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.ASSY_HDR_ID = pteam.MASTER_ID\r\n" + 
					"WHERE\r\n" +      
			        "        hdr.IS_COMPLETED = 0\r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
					"        AND PM_ID = '"+pmId+"'\r\n" + 
					"        AND pteam.IS_ACTIVE = 1\r\n" + 
					"        AND hdr.TENANT_ID = '"+tENANTID+"';";
			list=this.jdbcTemplate.query(dataQry,new ProjectProgressRowMapper());
		}catch(Exception ex) {
			logger.error("getprojectInProgress Error" + ex);
		}
		return list;
	}

	@Override
	public List<DesignWidgetDtlEntity> assyWidgetDtl(String tenantId, String empId, String deptCode,
			String pmId, String projId) {
		List<DesignWidgetDtlEntity> list = new ArrayList<DesignWidgetDtlEntity>();
		try {	
//			String month=monthYr.split("-")[1];
//			String year=monthYr.split("-")[0];
			String query ="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN DATE(hdr.COMPLETED_DATETIME) < hdr.PLANNED_START_DATE THEN 0\r\n" + 
					"        ELSE ABS(AVG(DATEDIFF(DATE(hdr.COMPLETED_DATETIME),\r\n" + 
					"                        hdr.PLANNED_START_DATE)))\r\n" + 
					"    END AS AVG_PROJ_DATETIME\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.ASSY_HDR_ID = pteam.MASTER_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
					"        AND hdr.COMPLETED_DATETIME IS NOT NULL\r\n" + 
					"        AND PM_ID = '"+pmId+"'\r\n" + 
					"        AND hdr.IS_COMPLETED = '1';" ; 
	
			list = this.jdbcTemplate.query(query, new DesignWidgetDtlMisRowMapper());
			
		}catch(Exception ex) {
			logger.error("assyWidgetDtl DAO Error" + ex);	
		}
		return list;
	}

	@Override
	public int assyWidgetTaskCnt(String tenantId, String empId, String deptCode, String pmId,
			String projId) {
		int Qry = 0;
		try {	
//			String month=monthYr.split("-")[1];
//			String year=monthYr.split("-")[0];
			String ChkQry ="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN\r\n" + 
					"            Date(tdtl.COMPLETED_DATE) < tdtl.PLANNED_COMPLETED_DATE\r\n" + 
					"        THEN\r\n" + 
					"             0\r\n" +
					"        ELSE \r\n" + 
					"            ABS(AVG(DATEDIFF(Date(tdtl.COMPLETED_DATE),\r\n" + 
					"                        tdtl.PLANNED_COMPLETED_DATE)))\r\n" + 
					"    END AS AVG_TASK_TIME\r\n" + 
					"FROM\r\n" + 
					"    assy_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_hdr thdr ON thdr.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team pteam ON hdr.ASSY_HDR_ID = pteam.MASTER_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl tdtl ON thdr.TE_HDR_ID = tdtl.TE_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = ?\r\n" + 
					"        AND pteam.ASSIGNED_EMP_ID = ?\r\n" + 
					"        AND PM_ID = ?\r\n" + 
					"        AND tdtl.IS_COMPLETED = '1'\r\n" + 
					"        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND thdr.DEPARTMENT_CODE = ?;"; 
		 
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(ChkQry, tenantId, empId, pmId, deptCode);
			if(resultMap.get("AVG_TASK_TIME") != null){
			Qry = Integer.parseInt(resultMap.get("AVG_TASK_TIME").toString());
			}
		}catch(Exception ex) {
			logger.error("assyWidgetTaskCnt Error" + ex);	
		}
		return Qry;
	}
}
