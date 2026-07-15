package com.vmfg.mis.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.mis.dao.interfaces.IDesignMisDAO;
import com.vmfg.mis.entity.DesignWidgetDtlListEntity;
import com.vmfg.mis.entity.GetPlannedProjectEntity;
import com.vmfg.mis.entity.GetTaskCompPerEntity;
import com.vmfg.mis.entity.ReportSchedulerEntity;
import com.vmfg.mis.entity.TaskDtlEntity;
import com.vmfg.mis.entity.getDesignWidgetDtlByCategoryEntity;
import com.vmfg.mis.rowmapper.DesignWidgetDtlRowMapper;
import com.vmfg.mis.rowmapper.GetDesignWidgetDtlByCategoryRowMapper;
import com.vmfg.mis.rowmapper.GetOldTaskRowMapper;
import com.vmfg.mis.rowmapper.GetPlannedProjectRowMapper;
import com.vmfg.mis.rowmapper.GetTaskCompPerRowMapper;
import com.vmfg.mis.rowmapper.ReportSchedulerRowMapper;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;
import com.vmfg.task.rowmapper.GetTaskEntryDtlRowMapper;

@Transactional
@Repository
public class DesignMisDAO implements IDesignMisDAO{
	private static final Logger logger = LoggerFactory.getLogger(DesignMisDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<DesignWidgetDtlListEntity> getDesignWidgetDtl(String deptCode, String empId, String tenantId,
			String month, String year,String category,String projectId,String lifespan) {
		List<DesignWidgetDtlListEntity> list=new ArrayList<>();
		String catCol="";
		String projId="";
		try {
			if(category.equalsIgnoreCase("getall")) {
			//	catCol="AND hdr.TASK_CATEGORY_CODE NOT like 'TC022,TC009,TC023'";
				catCol="AND NOT FIND_IN_SET(hdr.TASK_CATEGORY_CODE,'TC022,TC009,TC023')";
			}else {
				catCol=" AND FIND_IN_SET(hdr.TASK_CATEGORY_CODE,'"+category+"')";
			}
			if(projectId.equalsIgnoreCase("getall")) {
				projId="%%";
			}else {
				projId=projectId;
			}
			String qry="";
			if(lifespan.equalsIgnoreCase("0")) {
			 qry="SELECT \r\n" + 
					"    sum(dtl.QTY) AS QTY, \r\n" + 
					"    dtl.IS_COMPLETED\r\n" + 
					"FROM\r\n" + 
					"    task_entry_hdr hdr \r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.DEPARTMENT_CODE = '"+deptCode+"' AND hdr.DEPENDENT_TE_HDR_ID is null \r\n" + 
					"        AND dtl.ASSIGNED_TO like '"+empId+"'\r\n" + 
					"        AND hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND month(dtl.DUE_DATE) = '"+month+"'\r\n" + 
					"        AND year(dtl.DUE_DATE) = '"+year +"' AND hdr.PM_HDR_ID like '"+projId+"'\r\n" + 
						     catCol+ 
					"GROUP BY \r\n" + 
					"    dtl.IS_COMPLETED;";
			}else {
				qry="SELECT \r\n" + 
						"    sum(dtl.QTY) AS QTY, \r\n" + 
						"    dtl.IS_COMPLETED\r\n" + 
						"FROM\r\n" + 
						"    task_entry_hdr hdr \r\n" + 
						"        INNER JOIN\r\n" + 
						"    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    hdr.DEPARTMENT_CODE = '"+deptCode+"' AND hdr.DEPENDENT_TE_HDR_ID is null \r\n" + 
						"        AND dtl.ASSIGNED_TO like '"+empId+"'\r\n" + 
						"        AND hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
						"       AND hdr.PM_HDR_ID like '"+projId+"'\r\n" + 
							     catCol+ 
						"GROUP BY \r\n" + 
						"    dtl.IS_COMPLETED;";
			}
			RowMapper<DesignWidgetDtlListEntity> rowmapper = new DesignWidgetDtlRowMapper();	
			list=this.jdbcTemplate.query(qry,rowmapper);	 
		} catch (Exception ex) {
			logger.error("getDesignWidgetDtl  method exception-->" + ex);
		}
		logger.debug("getDesignWidgetDtl  method end");
		return list;		
	}

	@Override
	public String getMstPocByDeptCode(String departCode,String tenantId) {
		String empId = "";
		try {
			String qry = "select MASTER_POC from project_wbs_initiation_mst where DEPARTMENT_CODE = ? and TENANT_ID= ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, departCode, tenantId );
			empId = resultMap.get("MASTER_POC").toString();

		} catch (Exception ex) {
			logger.error("getMstPocByDeptCode Method Exception --->" + ex);

		}
		return empId;
	}

	@Override
	public List<GetTaskEntryDtlEntity> getPlannedActivity(String year, String month, String assigned, String department,
			String tenantId,String lifespan) {
		List<GetTaskEntryDtlEntity> list=new ArrayList<GetTaskEntryDtlEntity>();
		try {
			String monthYear="";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "AND YEAR(dtl.DUE_DATE) = '"+year+"' AND MONTH(dtl.DUE_DATE) = '"+month+"'";
			}
			String qry="SELECT \n"
					+ "    dtl.*,\n"
					+ "    dp.DEPARTMENT_NAME AS DEPARTMENT_NAME,\n"
					+ "    ttm.TT_DESC AS TT_DESC,\n"
					+ "    dtl.REQUIREMENT_FROM,\n"
					+ "    dtl.QTY,\n"
					+ "    hdr.TASK_CATEGORY_CODE,\n"
					+ "    hdr.TASK_TYPE_CODE,\n"
					+ "    tcm.TC_DESC AS TC_DESC,\n"
					+ "    dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,\n"
					+ "    em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_FIRSTNAME,phdr.PROJECT_CODE,phdr.PROJECT_NAME,phdr.CUSTOMER_NAME\n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID INNER JOIN project_hdr phdr ON phdr.PM_HDR_ID =hdr.PM_HDR_ID \n"
					+ "        LEFT JOIN\n"
					+ "    department dp ON hdr.DEPARTMENT_CODE = dp.DEPARTMENT_CODE\n"
					+ "        LEFT JOIN\n"
					+ "    task_type_mst ttm ON ttm.TT_CODE = hdr.TASK_TYPE_CODE\n"
					+ "        LEFT JOIN\n"
					+ "    task_category_mst tcm ON tcm.TC_CODE = hdr.TASK_CATEGORY_CODE\n"
					+ "        LEFT JOIN\n"
					+ "    document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = dtl.APPROVAL_STATUS\n"
					+ "        LEFT JOIN\n"
					+ "    employee_mst em ON em.EMPLOYEE_ID = dtl.ASSIGNED_TO\n"
					+ "WHERE\n"
					+ "    hdr.DEPARTMENT_CODE = '"+department+"'\n"
					+ "        AND hdr.TENANT_ID = '"+tenantId+"'\n"
					+ "        AND hdr.DEPENDENT_TE_HDR_ID IS NULL  "+monthYear+"\n"
					+ "        AND ASSIGNED_TO LIKE '"+assigned+"'\n"
					+ "ORDER BY IS_COMPLETED ASC , dtl.DUE_DATE ASC";
			
			RowMapper<GetTaskEntryDtlEntity> rowmapper = new GetTaskEntryDtlRowMapper();	
			list=this.jdbcTemplate.query(qry,rowmapper);	 
		} catch (Exception ex) {
			logger.error("getPlannedActivity  method exception-->" + ex);
		}
		logger.debug("getPlannedActivity  method end");
		return list;
	}

	@Override
	public List<GetPlannedProjectEntity> getProjList(String pmId, String pmHdrId, String empId,String tenantId) {
		List<GetPlannedProjectEntity> list=new ArrayList<GetPlannedProjectEntity>();
		try {
			String qry="SELECT \n"
					+ "    phdr.PROJECT_CODE ,phdr.PM_HDR_ID ,phdr.PROJECT_NAME,phdr.CUSTOMER_NAME,hdr.TENANT_ID\n"
					+ "FROM\n"
					+ "    design_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    process_assigned_team pat ON pat.MASTER_ID = hdr.DE_HDR_ID\n"
					+ "		INNER JOIN\n"
					+ "	project_hdr phdr ON phdr.PM_HDR_ID = hdr.PM_HDR_ID\n"
					+ "WHERE\n"
					+ "    pat.IS_ACTIVE = 1 and pat.PM_ID = ? \n"
					+ "        AND pat.ASSIGNED_EMP_ID like ? and hdr.PM_HDR_ID like ? and hdr.TENANT_ID = ? group by hdr.PM_HDR_ID order by PROJECT_CODE ";
			
			RowMapper<GetPlannedProjectEntity> rowmapper = new GetPlannedProjectRowMapper();	
			list=this.jdbcTemplate.query(qry,rowmapper,pmId,empId,pmHdrId,tenantId);	 
		} catch (Exception ex) {
			logger.error("getProjList  method exception-->" + ex);
		}
		logger.debug("getProjList  method end");
		return list;
	}

	@Override
	public String getTotalDrawingCount(String pmHdrId,String year ,String month,String tenantId,String lifespan) {
//		List<GetPlannedProjectEntity> list=new ArrayList<GetPlannedProjectEntity>();
		String getTotalDrawingCount ="";
		try {
			String monthYear="";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and year(DUE_DATE) = '"+year+"' and month(DUE_DATE) = '"+month+"'";
			}
			String qry="SELECT \n"
					+ "    CASE\n"
					+ "        WHEN COUNT(*) > 0 THEN SUM(dtl.QTY)\n"
					+ "        ELSE 0\n"
					+ "    END AS TotalCount\n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like '"+pmHdrId+"' \n"
			//		+ "        AND TASK_CATEGORY_CODE IN ('TC022' , 'TC009', 'TC023') "+monthYear+" and hdr.TENANT_ID = ? ";
			+ "        "+monthYear+" and hdr.TENANT_ID = ? ";
			
		//	RowMapper<GetPlannedProjectEntity> rowmapper = new GetPlannedProjectRowMapper();	
		//	list=this.jdbcTemplate.query(qry,rowmapper,pmHdrId,year,month);	 
		//	if(list.size()>0) {
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, tenantId );
			getTotalDrawingCount = resultMap.get("TotalCount").toString();
		//	}
		} catch (Exception ex) {
			logger.error("getTotalDrawingCount  method exception-->" + ex);
		}
		logger.debug("getTotalDrawingCount  method end");
		return getTotalDrawingCount;
	}

	@Override
	public String getCompletionDrawingCount(String pmHdrId,String year ,String month,String tenantId,String lifespan) {
	//	List<GetPlannedProjectEntity> list=new ArrayList<GetPlannedProjectEntity>();
		String getCompletionDrawingCount ="";
		try {
			String monthYear="";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and year(DUE_DATE) = '"+year+"' and month(DUE_DATE) = '"+month+"'";
			}
			String qry="SELECT \n"
					+ "    CASE\n"
					+ "        WHEN COUNT(*) > 0 THEN SUM(dtl.QTY)\n"
					+ "        ELSE 0\n"
					+ "    END AS CompleteCount\n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
			//		+ "        AND TASK_CATEGORY_CODE IN ('TC022' , 'TC009', 'TC023') "+monthYear+" \n"
					+ "         "+monthYear+" \n"
					+ "        AND dtl.IS_COMPLETED = 1 AND hdr.TENANT_ID = ? ";
			
		//	RowMapper<GetPlannedProjectEntity> rowmapper = new GetPlannedProjectRowMapper();	
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId,tenantId);
			getCompletionDrawingCount = resultMap.get("CompleteCount").toString();
		} catch (Exception ex) {
			logger.error("getCompletionDrawingCount  method exception-->" + ex);
		}
		logger.debug("getCompletionDrawingCount  method end");
		return getCompletionDrawingCount;
	}

	@Override
	public String getDapPlanDate(String pmHdrId,String year ,String month,String tenantId, String lifespan) {
//		List<GetPlannedProjectEntity> list=new ArrayList<GetPlannedProjectEntity>();
		String getDapPlanDate ="0";
		try {
			String monthYear="";
			
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and year(DUE_DATE) = '"+year+"' and month(DUE_DATE) = '"+month+"'";
			}
			String countCheckStr = "SELECT count(*) as COUNT from\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC022') "+monthYear+" AND hdr.TENANT_ID = ?  order by PLANNED_COMPLETED_DATE desc  limit 1";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(countCheckStr, pmHdrId,tenantId );
			int countCheck = Integer.parseInt(resultMap.get("COUNT").toString());
			if(countCheck >0) {
			String qry="SELECT \n"
					+ "# CASE WHEN COUNT(*) > 0 THEN dtl.PLANNED_COMPLETED_DATE ELSE 0 END AS DAP_PLANNED_COMPLETED_DATE \n"
					+ " dtl.PLANNED_COMPLETED_DATE AS DAP_PLANNED_COMPLETED_DATE FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC022') "+monthYear+"  order by PLANNED_COMPLETED_DATE desc  limit 1";
			
	//		RowMapper<GetPlannedProjectEntity> rowmapper = new GetPlannedProjectRowMapper();
	//		list = this.jdbcTemplate.query(qry, rowmapper);
			Map<String, Object> resultData = jdbcTemplate.queryForMap(qry, pmHdrId );
			getDapPlanDate = resultData.get("DAP_PLANNED_COMPLETED_DATE").toString();
		//	if(list.size()>0) {
		//		getDapPlanDate = list.get(0).getTotalDrawing();
		//	}
			
			}
		} catch (Exception ex) {
			logger.error("getDapPlanDate  method exception-->" + ex);
		}
		logger.debug("getProjgetDapPlanDateList  method end");
		return getDapPlanDate;
	}

	@Override
	public String getManualPlanDate(String pmHdrId,String year ,String month,String tenantId,String lifespan) {
	//	List<GetPlannedProjectEntity> list=new ArrayList<GetPlannedProjectEntity>();
		String getManualPlanDate = "0";
		try {
			
			String monthYear="";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and year(DUE_DATE) = '"+year+"' and month(DUE_DATE) = '"+month+"'";
			}
			String countCheckStr = "SELECT count(*) as COUNT from\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC009' , 'TC023') "+monthYear+" AND hdr.TENANT_ID = ?  order by PLANNED_COMPLETED_DATE desc  limit 1";
			 Map<String, Object> resultMap = jdbcTemplate.queryForMap(countCheckStr, pmHdrId,tenantId);
			 int countCheck = Integer.parseInt(resultMap.get("COUNT").toString());
			if(countCheck >0) {
		
			String qry="SELECT \n"
					+ "#CASE WHEN COUNT(*) > 0 THEN dtl.PLANNED_COMPLETED_DATE ELSE 0 END AS MANUAL_PLANNED_COMPLETED_DATE \n"
					+ " dtl.PLANNED_COMPLETED_DATE AS MANUAL_PLANNED_COMPLETED_DATE FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC009' , 'TC023') "+monthYear+" order by PLANNED_COMPLETED_DATE desc  limit 1";
		
			Map<String, Object> resultData = jdbcTemplate.queryForMap(qry, pmHdrId);
			getManualPlanDate= resultData.get("MANUAL_PLANNED_COMPLETED_DATE").toString();
		//	RowMapper<GetPlannedProjectEntity> rowmapper = new GetPlannedProjectRowMapper();	
		//	list=this.jdbcTemplate.query(qry,rowmapper);	 
		//	if(list.size()>0) {
		//		getManualPlanDate = list.get(0).getManualPlannedDate();
		//	}
			}
		} catch (Exception ex) {
			logger.error("getManualPlanDate  method exception-->" + ex);
		}
		logger.debug("getManualPlanDate  method end");
		return getManualPlanDate;
	}

	@Override
	public List<GetTaskCompPerEntity> getTaskCompPer(String assignedEmp, String deptCode, String tenantId,
			String year, String month,String pmHdrId) {
		List<GetTaskCompPerEntity> list=new ArrayList<GetTaskCompPerEntity>();
		try {
			String qry="SELECT \n"
					+ "    RT_ID,\n"
					+ "    rtw.EMPLOYEE_ID,\n"
					+ "    PM_HDR_ID,\n"
					+ "    rtw.DEPARTMENT_CODE,\n"
					+ "    REPORT_YEAR,\n"
					+ "    REPORT_MONTH,\n"
					+ "    WEEK_START,\n"
					+ "    SUM(NO_PLANNED_TASK) AS NO_PLANNED_TASK,\n"
					+ "    SUM(NO_COMPLETED_TASK) AS NO_COMPLETED_TASK,\n"
					+ "    SUM(DELAY_TASK) AS DELAY_TASK,\n"
					+ "    AVG(PERCENTAGE_COMPLETED) AS PERCENTAGE_COMPLETED,\n"
					+ "    rtw.TENANT_ID,\n"
					+ "    em.EMPLOYEE_FIRSTNAME\n"
					+ "FROM\n"
					+ "    report_task_workload_week rtw\n"
					+ "        INNER JOIN\n"
					+ "    employee_mst em ON em.EMPLOYEE_ID = rtw.EMPLOYEE_ID\n"
					+ "WHERE\n"
					+ "    REPORT_YEAR = ? \n"
					+ "        AND REPORT_MONTH = ? and rtw.EMPLOYEE_ID like '"+assignedEmp+"' and rtw.DEPARTMENT_CODE = ? and rtw.TENANT_ID = ?  and rtw.PM_HDR_ID like '"+pmHdrId+"' \n"
					+ "GROUP BY EMPLOYEE_ID , WEEK_START ";
			
			RowMapper<GetTaskCompPerEntity> rowmapper = new GetTaskCompPerRowMapper();	
			list=this.jdbcTemplate.query(qry,rowmapper,year,month,deptCode,tenantId);	 
		} catch (Exception ex) {
			logger.error("getTaskCompPer  method exception-->" + ex);
		}
		logger.debug("getTaskCompPer  method end");
		return list;
	}
	@Override
	public List<GetTaskCompPerEntity> getTaskCompPerByYear(String assignedEmp, String deptCode, String startMonth,String endMonth, String tenantId,String pmHdrId) {
		List<GetTaskCompPerEntity> list=new ArrayList<GetTaskCompPerEntity>();
		try {
			String qry="SELECT \n"
					+ "  RT_ID,  REPORT_MONTH_YEAR,\n"
					+ "    rtw.EMPLOYEE_ID,\n"
					+ "    PM_HDR_ID,\n"
					+ "    rtw.DEPARTMENT_CODE,\n"
					+ "    REPORT_YEAR,\n"
					+ "    REPORT_MONTH,\n"
					+ "    SUM(NO_PLANNED_TASK) AS NO_PLANNED_TASK,\n"
					+ "    SUM(NO_COMPLETED_TASK) AS NO_COMPLETED_TASK,\n"
					+ "    SUM(DELAY_TASK) AS DELAY_TASK,\n"
					+ "    AVG(PERCENTAGE_COMPLETED) AS PERCENTAGE_COMPLETED,\n"
					+ "    rtw.TENANT_ID,\n"
					+ "    em.EMPLOYEE_FIRSTNAME\n"
					+ "FROM\n"
					+ "    report_task_workload_month rtw\n"
					+ "        INNER JOIN\n"
					+ "    employee_mst em ON em.EMPLOYEE_ID = rtw.EMPLOYEE_ID\n"
					+ "WHERE\n"
					+ "    REPORT_MONTH_YEAR  between ? and ? \n"
					+ "        AND rtw.EMPLOYEE_ID LIKE '"+assignedEmp+"'  and rtw.DEPARTMENT_CODE = ? and rtw.TENANT_ID = ? and rtw.PM_HDR_ID like '"+pmHdrId+"' \n"
					+ "GROUP BY REPORT_MONTH_YEAR , rtw.EMPLOYEE_ID";
			
			RowMapper<GetTaskCompPerEntity> rowmapper = new GetTaskCompPerRowMapper();	
			list=this.jdbcTemplate.query(qry,rowmapper,startMonth,endMonth, deptCode,tenantId);	 
		} catch (Exception ex) {
			logger.error("getTaskCompPer  method exception-->" + ex);
		}
		logger.debug("getTaskCompPer  method end");
		return list;
	}

	@Override
	public int getcompletedManualCount(String pmHdrId, String year, String month,String tenantId,String lifespan) {
		int list=0;
		try {
			String monthYear="";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and year(DUE_DATE) = '"+year+"' and month(DUE_DATE) = '"+month+"'";
			}
			String qry="SELECT count(*) as COUNT \n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ?  and dtl.COMPLETED_DATE is null \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC009' , 'TC023') "+monthYear+" and hdr.TENANT_ID = ?  order by PLANNED_COMPLETED_DATE desc  limit 1";
			
			 Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId,tenantId);
			list = Integer.parseInt(resultMap.get("COUNT").toString()); 
		} catch (Exception ex) {
			logger.error("getcompletedManualCount  method exception-->" + ex);
		}
		logger.debug("getcompletedManualCount  method end");
		return list;
	}

	@Override
	public int getcompletedDapCount(String pmHdrId, String year, String month,String tenantId,String lifespan) {
		int list=0;
		try {
			String monthYear="";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and year(DUE_DATE) = '"+year+"' and month(DUE_DATE) = '"+month+"'";
			}
			String qry="SELECT count(*) as COUNT\n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ?  and dtl.COMPLETED_DATE is null \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC022') "+monthYear+" and hdr.TENANT_ID = ?  order by PLANNED_COMPLETED_DATE desc  limit 1";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId,tenantId);
			list = Integer.parseInt(resultMap.get("COUNT").toString());
		} catch (Exception ex) {
			logger.error("getcompletedDapCount  method exception-->" + ex);
		}
		logger.debug("getcompletedDapCount  method end");
		return list;
	}

	@Override
	public String getDapCompleteDate(String pmHdrId, String year, String month,String tenantId,String lifespan) {
	//	List<GetPlannedProjectEntity> list=new ArrayList<GetPlannedProjectEntity>();
		String getDapCompleteDate ="0";
		try {

			String monthYear="";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and year(DUE_DATE) = '"+year+"' and month(DUE_DATE) = '"+month+"'";
			}
			String countCheckStr = "SELECT Count(*) as COUNT\n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC022') "+monthYear+" AND hdr.TENANT_ID = ?  order by COMPLETED_DATE desc  limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(countCheckStr,pmHdrId,tenantId);
			int countCheck = Integer.parseInt(resultMap.get("COUNT").toString());
			
			if(countCheck >0) {
			String qry="SELECT \n"
					+ "dtl.COMPLETED_DATE AS DAP_ACTUAL_DATE \n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC022') "+monthYear+" order by COMPLETED_DATE desc  limit 1";
			
		//	RowMapper<GetPlannedProjectEntity> rowmapper = new GetPlannedProjectRowMapper();	
		//	list=this.jdbcTemplate.query(qry,rowmapper,,,);	 
			 Map<String, Object> resultData = jdbcTemplate.queryForMap(qry, pmHdrId );
			 getDapCompleteDate = resultData.get("DAP_ACTUAL_DATE").toString();
			}
		} catch (Exception ex) {
			logger.error("getDapCompleteDate  method exception-->" + ex);
		}
		logger.debug("getDapCompleteDate  method end");
		return getDapCompleteDate;
	}

	@Override
	public String getManualCompleteDate(String pmHdrId, String year, String month,String tenantId,String lifespan) {
	//	List<GetPlannedProjectEntity> list=new ArrayList<GetPlannedProjectEntity>();
		String getManualCompleteDate ="";
		try {
			String monthYear="";
			if(lifespan.equalsIgnoreCase("0")) {
				monthYear= "and year(DUE_DATE) = '"+year+"' and month(DUE_DATE) = '"+month+"'";
			}
			String countCheckStr = "SELECT \n"
					+ " Count(*) as Count \n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC009' , 'TC023') "+monthYear+"  AND hdr.TENANT_ID = ?  order by COMPLETED_DATE desc  limit 1";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(countCheckStr, pmHdrId, tenantId);
			int countCheck = Integer.parseInt(resultMap.get("Count").toString());
			if(countCheck >0) {
				
			
			String qry="SELECT \n"
					+ "dtl.COMPLETED_DATE  AS MANUAL_ACTUAL_DATE\n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND PM_HDR_ID like ? \n"
					+ "        AND TASK_CATEGORY_CODE IN ('TC009' , 'TC023') "+monthYear+" order by COMPLETED_DATE desc  limit 1";
			
		//	RowMapper<GetPlannedProjectEntity> rowmapper = new GetPlannedProjectRowMapper();	
		//	list=this.jdbcTemplate.query(qry,rowmapper,pmHdrId,year,month);	 
			Map<String, Object> resultData = jdbcTemplate.queryForMap(qry, pmHdrId);
			getManualCompleteDate  = resultData.get("MANUAL_ACTUAL_DATE").toString();
			}
		} catch (Exception ex) {
			logger.error("getManualCompleteDate  method exception-->" + ex);
		}
		logger.debug("getManualCompleteDate  method end");
		return getManualCompleteDate;
	}

	@Override
	public List<getDesignWidgetDtlByCategoryEntity> getDesignWidgetDtlByCategory(
			String deptCode, String empId, String tenantId, String month, String year, String category, String projectId) {
		List<getDesignWidgetDtlByCategoryEntity> list=new ArrayList<getDesignWidgetDtlByCategoryEntity>();
		String catCol="";
		String projId ="";
		try {
			if(category.equalsIgnoreCase("getall")) {
				catCol="AND hdr.TASK_CATEGORY_CODE like '%%'";
			}else {
				catCol=" AND FIND_IN_SET(hdr.TASK_CATEGORY_CODE,'"+category+"')";
			}
			if(projectId.equalsIgnoreCase("getall")) {
				projId="%%";
			}else {
				projId=projectId;
			}
			String qry="SELECT \r\n" + 
					"    dtl.TE_DTl_ID, dtl.ACTIVITY_NAME,dtl.PLANNED_START_DATE,dtl.PLANNED_COMPLETED_DATE,dtl.ASSIGNED_TO,mst.EMPLOYEE_FIRSTNAME,dtl.COMPLETED_DATE,ph.PROJECT_NAME,ph.PROJECT_CODE \r\n" + 
					"FROM\r\n" + 
					"    task_entry_hdr hdr \r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID INNER JOIN employee_mst mst ON mst.EMPLOYEE_ID = dtl.ASSIGNED_TO INNER JOIN project_hdr ph ON ph.PM_HDR_ID = hdr.PM_HDR_ID \r\n" + 
					"WHERE\r\n" + 
					"    hdr.DEPARTMENT_CODE = '"+deptCode+"' AND hdr.DEPENDENT_TE_HDR_ID is null \r\n" + 
					"        AND dtl.ASSIGNED_TO like '"+empId+"'\r\n" + 
					"        AND hdr.TENANT_ID = '"+tenantId+"'\r\n" + 
					"        AND month(dtl.DUE_DATE) = '"+month+"' and hdr.DEPENDENT_TE_HDR_ID is null \r\n" + 
					"        AND year(dtl.DUE_DATE) = '"+year +"' AND hdr.PM_HDR_ID like '"+projId+"'  \r\n" + 
						     catCol+ 
					" " ;
			
			RowMapper<getDesignWidgetDtlByCategoryEntity> rowmapper = new GetDesignWidgetDtlByCategoryRowMapper();	
			list=this.jdbcTemplate.query(qry,rowmapper);	 
		} catch (Exception ex) {
			logger.error("getDesignWidgetDtlByCategoryEntity  method exception-->" + ex);
		}
		logger.debug("getDesignWidgetDtlByCategoryEntity  method end");
		return list;	
	}

	@Override
	public List<ReportSchedulerEntity> getReportTaskSchForMonth(String month, String year,String tenantId) {
		List<ReportSchedulerEntity> list=new ArrayList<ReportSchedulerEntity>();
		try {
			String qry="SELECT \n"
					+ "    tdtl.ASSIGNED_TO,\n"
					+ "    hdr.PM_HDR_ID,\n"
					+ "    COUNT(tdtl.PLANNED_START_DATE IS NOT NULL) AS NO_PLANNED_TASK,\n"
					+ "    SUM(CASE\n"
					+ "        WHEN\n"
					+ "            tdtl.COMPLETED_DATE IS NOT NULL\n"
					+ "                AND tdtl.IS_COMPLETED = '1'\n"
					+ "        THEN\n"
					+ "            1\n"
					+ "        ELSE 0\n"
					+ "    END) AS NO_COMPLETED_TASK,\n"
					+ "    SUM(CASE\n"
					+ "        WHEN\n"
					+ "            (tdtl.DUE_DATE < tdtl.COMPLETED_DATE\n"
					+ "                OR (tdtl.COMPLETED_DATE IS NULL\n"
					+ "                && tdtl.DUE_DATE < CURRENT_DATE()))\n"
					+ "        THEN\n"
					+ "            1\n"
					+ "        ELSE 0\n"
					+ "    END) AS DELAY_TASK,\n"
					+ "    hdr.DEPARTMENT_CODE,\n"
					+ "    YEAR(DUE_DATE) AS REPORT_YEAR,\n"
					+ "    MONTH(DUE_DATE) AS REPORT_MONTH,\n"
					+ "    CONCAT(YEAR(DUE_DATE), '-', MONTH(DUE_DATE)) AS REPORT_MONTH_YEAR,\n"
					+ "    CASE\n"
					+ "        WHEN\n"
					+ "            COUNT(CASE\n"
					+ "                WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\n"
					+ "            END) > 0\n"
					+ "        THEN\n"
					+ "            ROUND((SUM(CASE\n"
					+ "                        WHEN tdtl.COMPLETED_DATE IS NOT NULL THEN 1\n"
					+ "                        ELSE 0\n"
					+ "                    END) / COUNT(CASE\n"
					+ "                        WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\n"
					+ "                    END)) * 100,\n"
					+ "                    2)\n"
					+ "        ELSE 0\n"
					+ "    END AS COMPLETION_PERCENTAGE,hdr.TENANT_ID\n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl tdtl ON hdr.TE_HDR_ID = tdtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    YEAR(DUE_DATE) = ? \n"
					+ "        AND MONTH(DUE_DATE) = ? \n"
					+ "        AND hdr.DEPENDENT_TE_HDR_ID IS NULL AND hdr.TENANT_ID = ? \n"
					+ "GROUP BY tdtl.ASSIGNED_TO , hdr.PM_HDR_ID , YEAR(DUE_DATE) =? \n"
					+ "    AND MONTH(DUE_DATE) = ? , hdr.DEPARTMENT_CODE " ;
			
			RowMapper<ReportSchedulerEntity> rowmapper = new ReportSchedulerRowMapper();	
			list=this.jdbcTemplate.query(qry,rowmapper,year,month,tenantId,year,month);	 
		} catch (Exception ex) {
			logger.error("getReportTaskSchForMonth  method exception-->" + ex);
		}
		logger.debug("getReportTaskSchForMonth  method end");
		return list;	
	}

	@Override
	public int getReportTaskCount(String empId, String pmhdrId, String deptCode, String monthYear) {
		int list=0;
		try {
			String qry="SELECT \n"
					+ "    CASE\n"
					+ "        WHEN COUNT(*) > 0 THEN RT_ID\n"
					+ "        ELSE 0\n"
					+ "    END AS RT_ID\n"
					+ "FROM\n"
					+ "    report_task_workload_month\n"
					+ "WHERE\n"
					+ "    EMPLOYEE_ID = ? AND PM_HDR_ID = ?\n"
					+ "        AND DEPARTMENT_CODE = ?\n"
					+ "        AND REPORT_MONTH_YEAR = ?\n"
					+ "LIMIT 1";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmhdrId,deptCode,monthYear);	
			list= Integer.parseInt(resultMap.get("RT_ID").toString());
		} catch (Exception ex) {
			logger.error("getReportTaskCount  method exception-->" + ex);
		}
		logger.debug("getReportTaskCount  method end");
		return list;
	}
	

	@Override
	public int insertReportTask(String empId,String pmHdrId,String DepartmentCode,String reportYear,String reportMonth,String monthYear,String noPlannedTask,String noCompeltedTask,String delayTask,String percentageCompleted,String tenantId ) {
		int list=0;
		try {
			String qry="INSERT INTO `report_task_workload_month` (`RT_ID`, `EMPLOYEE_ID`, `PM_HDR_ID`, `DEPARTMENT_CODE`, `REPORT_YEAR`, `REPORT_MONTH`, `REPORT_MONTH_YEAR`, `NO_PLANNED_TASK`, `NO_COMPLETED_TASK`, `DELAY_TASK`, `PERCENTAGE_COMPLETED`, `TENANT_ID`) VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?)";
			
			list=this.jdbcTemplate.update(qry,empId,pmHdrId,DepartmentCode,reportYear,reportMonth,monthYear,noPlannedTask,noCompeltedTask,delayTask,percentageCompleted,tenantId);	 
		} catch (Exception ex) {
			logger.error("insertReportTask  method exception-->" + ex);
		}
		logger.debug("insertReportTask  method end");
		return list;
	}

	@Override
	public int updateReportTask(String empId, String pmHdrId, String DepartmentCode, String monthYear, String noPlannedTask, String noCompeltedTask, String delayTask,
			String percentageCompleted,String tenantId) {
		int list=0;
		try {
			String qry="UPDATE `report_task_workload_month` SET `NO_PLANNED_TASK`= ?, `NO_COMPLETED_TASK`= ?, `DELAY_TASK`= ? , `PERCENTAGE_COMPLETED` = ?  WHERE `EMPLOYEE_ID` = ? and `PM_HDR_ID` = ? and `DEPARTMENT_CODE` = ? and `REPORT_MONTH_YEAR` = ? and  `RT_ID` > 0 and `TENANT_ID` = ? ";
			
			list=this.jdbcTemplate.update(qry,noPlannedTask,noCompeltedTask,delayTask,percentageCompleted,empId,pmHdrId,DepartmentCode,monthYear,tenantId);	 
		} catch (Exception ex) {
			logger.error("updateReportTask  method exception-->" + ex);
		}
		logger.debug("updateReportTask  method end");
		return list;
	}

	@Override
	public List<TaskDtlEntity> getOldTaskMonthPlanned(String monthYear,String tenantId) {
		List<TaskDtlEntity> retLi = null;
		try {

			String getQ = "SELECT \n"
					+ "    ASSIGNED_TO,\n"
					+ "    thdr.PM_HDR_ID,\n"
					+ "    thdr.DEPARTMENT_CODE,\n"
					+ "    COUNT(*) AS cnt\n"
					+ "FROM\n"
					+ "    task_entry_hdr thdr,\n"
					+ "    task_entry_dtl tdtl\n"
					+ "WHERE\n"
					+ "    thdr.TE_HDR_ID = tdtl.TE_HDR_ID\n"
					+ "        AND IS_COMPLETED = 0\n"
					+ "        AND DUE_DATE < '"+monthYear+"-01' \n"
					+ "        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND tdtl.PLANNED_START_DATE IS NOT NULL\n"
					+ "        AND thdr.PM_HDR_ID AND thdr.TENANT_ID = ? \n"
					+ "GROUP BY tdtl.ASSIGNED_TO , thdr.DEPARTMENT_CODE , thdr.PM_HDR_ID ,YEAR(DUE_DATE)  \n"
					+ "        AND MONTH(DUE_DATE) ";
			retLi = this.jdbcTemplate.query(getQ, new GetOldTaskRowMapper(),tenantId);

		} catch (Exception ex) {
			logger.info("getOldTaskMonthPlanned is method is error" + ex);
		}
		return retLi;
	}

	@Override
	public List<TaskDtlEntity> getOldTaskMonthCompleted(String year, String month, String monthYear,String tenantId) {
		List<TaskDtlEntity> retLi = null;
		try {

			String getQ = "SELECT \n"
					+ "    ASSIGNED_TO,\n"
					+ "    thdr.PM_HDR_ID,\n"
					+ "    thdr.DEPARTMENT_CODE,\n"
					+ "    COUNT(*) AS cnt\n"
					+ "FROM\n"
					+ "    task_entry_hdr thdr,\n"
					+ "    task_entry_dtl tdtl\n"
					+ "WHERE\n"
					+ "    thdr.TE_HDR_ID = tdtl.TE_HDR_ID\n"
					+ "        AND IS_COMPLETED = 1\n"
					+ "        AND DUE_DATE < '"+monthYear+"-01'\n"
					+ "        AND YEAR(COMPLETED_DATE) = ?\n"
					+ "        AND MONTH(COMPLETED_DATE) = ?\n"
					+ "        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND tdtl.PLANNED_START_DATE IS NOT NULL\n"
					+ "        AND thdr.PM_HDR_ID AND thdr.TENANT_ID = ?  \n"
					+ "GROUP BY tdtl.ASSIGNED_TO , thdr.DEPARTMENT_CODE , thdr.PM_HDR_ID ,YEAR(DUE_DATE) \n"
					+ "        AND MONTH(DUE_DATE) ";
			retLi = this.jdbcTemplate.query(getQ, new GetOldTaskRowMapper(),year,month,tenantId);

		} catch (Exception ex) {
			logger.info("getOldTaskMonthCompleted is method is error" + ex);
		}
		return retLi;
	}

	@Override
	public void updatePlannedTaskMonth(TaskDtlEntity taskDtlEntity, String recordDate,String tenantId) {
		try {

			String updateRecord = "update report_task_workload_month set NO_PLANNED_TASK = NO_PLANNED_TASK+ ? where EMPLOYEE_ID =? and PM_HDR_ID = ? and DEPARTMENT_CODE =? and REPORT_MONTH_YEAR = ? and RT_ID>0 and TENANT_ID = ? ";

			this.jdbcTemplate.update(updateRecord, taskDtlEntity.getCnt(), taskDtlEntity.getAssignedTo(),
					taskDtlEntity.getPmHdrid(), taskDtlEntity.getDeptCode(), recordDate, tenantId);

		} catch (Exception ex) {
			logger.info("updatePlannedTaskMonth is method is error" + ex);
		}
	}

	@Override
	public void updateCompTaskMonth(TaskDtlEntity taskDtlEntity, String refDate,String tenantId) {
		try {

			String updateRecord = "update report_task_workload_month set NO_COMPLETED_TASK = NO_COMPLETED_TASK+ ? where EMPLOYEE_ID =? and PM_HDR_ID = ? and DEPARTMENT_CODE =? and REPORT_MONTH_YEAR = ? and RT_ID>0 and TENANT_ID = ? ";

			this.jdbcTemplate.update(updateRecord, taskDtlEntity.getCnt(), taskDtlEntity.getAssignedTo(),
					taskDtlEntity.getPmHdrid(), taskDtlEntity.getDeptCode(), refDate,tenantId);

		} catch (Exception ex) {
			logger.info("updateCompTaskMonth is method is error" + ex);
		}
	}

	@Override
	public void taskPercentUpdateMonth(String refDate) {
		try {

			String updateRecord = "update report_task_workload_month set PERCENTAGE_COMPLETED = ((NO_COMPLETED_TASK / NO_PLANNED_TASK)*100) where REPORT_MONTH_YEAR = ? and RT_ID>0";

			this.jdbcTemplate.update(updateRecord, refDate);

		} catch (Exception ex) {
			logger.info("taskPercentUpdateMonth is method is error" + ex);
		}
	}

	@Override
	public List<ReportSchedulerEntity> getReportTaskSchForWeek(String startDate, String endDate, String tenantId) {
		List<ReportSchedulerEntity> list=new ArrayList<ReportSchedulerEntity>();
		try {
			String qry="SELECT \n"
					+ "    tdtl.ASSIGNED_TO,\n"
					+ "    hdr.PM_HDR_ID,\n"
					+ "    COUNT(tdtl.PLANNED_START_DATE IS NOT NULL) AS NO_PLANNED_TASK,\n"
					+ "    SUM(CASE\n"
					+ "        WHEN\n"
					+ "            tdtl.COMPLETED_DATE IS NOT NULL\n"
					+ "                AND tdtl.IS_COMPLETED = '1'\n"
					+ "        THEN\n"
					+ "            1\n"
					+ "        ELSE 0\n"
					+ "    END) AS NO_COMPLETED_TASK,\n"
					+ "    SUM(CASE\n"
					+ "        WHEN\n"
					+ "            (tdtl.DUE_DATE < tdtl.COMPLETED_DATE\n"
					+ "                OR (tdtl.COMPLETED_DATE IS NULL\n"
					+ "                && tdtl.DUE_DATE < CURRENT_DATE()))\n"
					+ "        THEN\n"
					+ "            1\n"
					+ "        ELSE 0\n"
					+ "    END) AS DELAY_TASK,\n"
					+ "    hdr.DEPARTMENT_CODE,\n"
					+ "    YEAR(DUE_DATE) AS REPORT_YEAR,\n"
					+ "    MONTH(DUE_DATE) AS REPORT_MONTH,\n"
					+ "    CONCAT(YEAR(DUE_DATE), '-', MONTH(DUE_DATE)) AS REPORT_MONTH_YEAR,\n"
					+ "    CASE\n"
					+ "        WHEN\n"
					+ "            COUNT(CASE\n"
					+ "                WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\n"
					+ "            END) > 0\n"
					+ "        THEN\n"
					+ "            ROUND((SUM(CASE\n"
					+ "                        WHEN tdtl.COMPLETED_DATE IS NOT NULL THEN 1\n"
					+ "                        ELSE 0\n"
					+ "                    END) / COUNT(CASE\n"
					+ "                        WHEN tdtl.PLANNED_START_DATE IS NOT NULL THEN 1\n"
					+ "                    END)) * 100,\n"
					+ "                    2)\n"
					+ "        ELSE 0\n"
					+ "    END AS COMPLETION_PERCENTAGE,\n"
					+ "    hdr.TENANT_ID\n"
					+ "FROM\n"
					+ "    task_entry_hdr hdr\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_dtl tdtl ON hdr.TE_HDR_ID = tdtl.TE_HDR_ID\n"
					+ "WHERE\n"
					+ "    DUE_DATE BETWEEN ? AND ? \n"
					+ "        AND hdr.DEPENDENT_TE_HDR_ID IS NULL AND  hdr.TENANT_ID = ? \n"
					+ "GROUP BY tdtl.ASSIGNED_TO , hdr.PM_HDR_ID , hdr.DEPARTMENT_CODE" ;
			
			RowMapper<ReportSchedulerEntity> rowmapper = new ReportSchedulerRowMapper();	
			list=this.jdbcTemplate.query(qry,rowmapper,startDate,endDate,tenantId);	 
		} catch (Exception ex) {
			logger.error("getReportTaskSchForWeek  method exception-->" + ex);
		}
		logger.debug("getReportTaskSchForWeek  method end");
		return list;	
	}

	@Override
	public int getReportTaskCountWeek(String empId, String pmhdrId, String deptCode, String startDate,String tenantId) {
		int list=0;
		try {
			String qry="SELECT \n"
					+ "    CASE\n"
					+ "        WHEN COUNT(*) > 0 THEN RT_ID\n"
					+ "        ELSE 0\n"
					+ "    END AS RT_ID\n"
					+ "FROM\n"
					+ "    report_task_workload_week\n"
					+ "WHERE\n"
					+ "    EMPLOYEE_ID = ? AND PM_HDR_ID = ?\n"
					+ "        AND DEPARTMENT_CODE = ?\n"
					+ "        AND WEEK_START = ? AND TENANT_ID = ? \n"
					+ "LIMIT 1";
			
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry,empId,pmhdrId,deptCode,startDate,tenantId);	
			list= Integer.parseInt(resultMap.get("RT_ID").toString());	 
		} catch (Exception ex) {
			logger.error("getReportTaskCountWeek  method exception-->" + ex);
		}
		logger.debug("getReportTaskCountWeek  method end");
		return list;
	}

	
	@Override
	public int insertReportTaskWeek(String empId,String pmHdrId,String DepartmentCode,String reportYear,String reportMonth,String monthYear,String noPlannedTask,String noCompeltedTask,String delayTask,String percentageCompleted,String tenantId ) {
		int list=0;
		try {
			String qry="INSERT INTO `report_task_workload_week` (`RT_ID`, `EMPLOYEE_ID`, `PM_HDR_ID`, `DEPARTMENT_CODE`, `REPORT_YEAR`, `REPORT_MONTH`, `WEEK_START`, `NO_PLANNED_TASK`, `NO_COMPLETED_TASK`, `DELAY_TASK`, `PERCENTAGE_COMPLETED`, `TENANT_ID`) VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?)";
			
			list=this.jdbcTemplate.update(qry,empId,pmHdrId,DepartmentCode,reportYear,reportMonth,monthYear,noPlannedTask,noCompeltedTask,delayTask,percentageCompleted,tenantId);	 
		} catch (Exception ex) {
			logger.error("insertReportTaskWeek  method exception-->" + ex);
		}
		logger.debug("insertReportTaskWeek  method end");
		return list;
	}

	@Override
	public int updateReportTaskWeek(String empId, String pmHdrId, String DepartmentCode, String monthYear, String noPlannedTask, String noCompeltedTask, String delayTask,
			String percentageCompleted,String tenantId) {
		int list=0;
		try {
			String qry="UPDATE `report_task_workload_week` SET `NO_PLANNED_TASK`= ?, `NO_COMPLETED_TASK`= ?, `DELAY_TASK`= ? , `PERCENTAGE_COMPLETED` = ?  WHERE `EMPLOYEE_ID` = ? and `PM_HDR_ID` = ? and `DEPARTMENT_CODE` = ? and `WEEK_START` = ? and  `RT_ID` > 0 and `TENANT_ID` = ? ";
			
			list=this.jdbcTemplate.update(qry,noPlannedTask,noCompeltedTask,delayTask,percentageCompleted,empId,pmHdrId,DepartmentCode,monthYear,tenantId);	 
		} catch (Exception ex) {
			logger.error("updateReportTaskWeek  method exception-->" + ex);
		}
		logger.debug("updateReportTaskWeek  method end");
		return list;
	}

	@Override
	public List<TaskDtlEntity> getOldTaskWeekPlanned(String startDate,String tenantId) {
		List<TaskDtlEntity> retLi = null;
		try {

			String getQ = "SELECT \n"
					+ "    ASSIGNED_TO,\n"
					+ "    thdr.PM_HDR_ID,\n"
					+ "    thdr.DEPARTMENT_CODE,\n"
					+ "    COUNT(*) AS cnt\n"
					+ "FROM\n"
					+ "    task_entry_hdr thdr,\n"
					+ "    task_entry_dtl tdtl\n"
					+ "WHERE\n"
					+ "    thdr.TE_HDR_ID = tdtl.TE_HDR_ID\n"
					+ "        AND IS_COMPLETED = 0\n"
					+ "        AND DUE_DATE < ?  \n"
					+ "        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND tdtl.PLANNED_START_DATE IS NOT NULL\n"
					+ "        AND thdr.PM_HDR_ID\n"
					+ "GROUP BY tdtl.ASSIGNED_TO , thdr.DEPARTMENT_CODE , thdr.PM_HDR_ID ";
			retLi = this.jdbcTemplate.query(getQ, new GetOldTaskRowMapper(), startDate);

		} catch (Exception ex) {
			logger.info("getOldTaskWeekPlanned is method is error" + ex);
		}
		return retLi;
	}

	@Override
	public List<TaskDtlEntity> getOldTaskWeekCompleted(String startDate, String endDate,String tenantId) {
		List<TaskDtlEntity> retLi = null;
		try {

			String getQ = "SELECT \n"
					+ "    ASSIGNED_TO,\n"
					+ "    thdr.PM_HDR_ID,\n"
					+ "    thdr.DEPARTMENT_CODE,\n"
					+ "    COUNT(*) AS cnt\n"
					+ "FROM\n"
					+ "    task_entry_hdr thdr,\n"
					+ "    task_entry_dtl tdtl\n"
					+ "WHERE\n"
					+ "    thdr.TE_HDR_ID = tdtl.TE_HDR_ID\n"
					+ "        AND IS_COMPLETED = 1\n"
					+ "        AND DUE_DATE < ?  \n"
					+ "       AND COMPLETED_DATE between ? and ? \n"
					+ "        AND thdr.DEPENDENT_TE_HDR_ID IS NULL\n"
					+ "        AND tdtl.PLANNED_START_DATE IS NOT NULL\n"
					+ "        AND thdr.PM_HDR_ID\n"
					+ "GROUP BY tdtl.ASSIGNED_TO , thdr.DEPARTMENT_CODE , thdr.PM_HDR_ID  ";
			retLi = this.jdbcTemplate.query(getQ, new GetOldTaskRowMapper(),startDate, startDate, endDate);

		} catch (Exception ex) {
			logger.info("getOldTaskWeekCompleted is method is error" + ex);
		}
		return retLi;
	}
	
	@Override
	public void updatePlannedTaskWeek(TaskDtlEntity taskDtlEntity, String recordDate,String tenantId) {
		try {

			String updateRecord = "update report_task_workload_week set NO_PLANNED_TASK = NO_PLANNED_TASK+ ? where EMPLOYEE_ID =? and PM_HDR_ID = ? and DEPARTMENT_CODE =? and WEEK_START = ? and RT_ID>0 and TENANT_ID = ? ";

			this.jdbcTemplate.update(updateRecord, taskDtlEntity.getCnt(), taskDtlEntity.getAssignedTo(),
					taskDtlEntity.getPmHdrid(), taskDtlEntity.getDeptCode(), recordDate,tenantId);

		} catch (Exception ex) {
			logger.info("updatePlannedTaskMonth is method is error" + ex);
		}
	}

	@Override
	public void updateCompTaskWeek(TaskDtlEntity taskDtlEntity, String refDate,String tenantId) {
		try {

			String updateRecord = "update report_task_workload_week set NO_COMPLETED_TASK = NO_COMPLETED_TASK+ ? where EMPLOYEE_ID =? and PM_HDR_ID = ? and DEPARTMENT_CODE =? and WEEK_START = ? and RT_ID>0 and TENANT_ID = ? ";

			this.jdbcTemplate.update(updateRecord, taskDtlEntity.getCnt(), taskDtlEntity.getAssignedTo(),
					taskDtlEntity.getPmHdrid(), taskDtlEntity.getDeptCode(), refDate,tenantId);

		} catch (Exception ex) {
			logger.info("updateCompTaskMonth is method is error" + ex);
		}
	}

	@Override
	public void taskPercentUpdateWeek(String refDate,String tenantId) {
		try {

			String updateRecord = "update report_task_workload_week set PERCENTAGE_COMPLETED = ((NO_COMPLETED_TASK / NO_PLANNED_TASK)*100) where WEEK_START = ? and RT_ID>0 and TENANT_ID = ? ";

			this.jdbcTemplate.update(updateRecord, refDate,tenantId);

		} catch (Exception ex) {
			logger.info("taskPercentUpdateMonth is method is error" + ex);
		}
	}
}
