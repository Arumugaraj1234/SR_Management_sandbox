package com.vmfg.project.dao.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vmfg.project.response.ProjectInternalResponse;
import com.vmfg.project.rowmapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.design.dao.impl.DesignDAO;
import com.vmfg.design.request.TenantRequest;
import com.vmfg.design.response.KeyArea;
import com.vmfg.design.rowmapper.DesignAreaRowMapper;
import com.vmfg.general.response.ResponseAsMessage;
import com.vmfg.general.response.ResponseMessageMap;
import com.vmfg.project.dao.interfaces.IProjectDAO;
import com.vmfg.project.entity.BudgetSheetPaymentEntity;
import com.vmfg.project.entity.GetProjTimePlanDropDownEntity;
import com.vmfg.project.entity.GetindentbudgetDtlEntity;
import com.vmfg.project.entity.IndentBudgetDtlEntity;
import com.vmfg.project.entity.ProjectHdr;
import com.vmfg.project.entity.ProjectSubAreaExtnEntity;
import com.vmfg.project.entity.ProjectTimelineEntity;
import com.vmfg.project.entity.ProjectTimelineResp;
import com.vmfg.project.entity.ProjectWBSTemplate;
import com.vmfg.project.entity.SalesBudgetExtnDtlEntity;
import com.vmfg.project.entity.SalesBudgetExtnListDtlEntity;
import com.vmfg.project.entity.SubAreaPmHdrListEntity;
import com.vmfg.project.entity.SumOfIndentHdrEntity;
import com.vmfg.project.entity.getLinkStatusByPMIdRespEntity;
import com.vmfg.project.request.AssyMstRequest;
import com.vmfg.project.request.DeleteTimeWBSByIDRequest;
import com.vmfg.project.request.KeyAreaDelRequest;
import com.vmfg.project.request.KeyAreaRequest;
import com.vmfg.project.request.ProjectByIDRequest;
import com.vmfg.project.request.ProjectInitiationMstRequest;
import com.vmfg.project.request.ProjectTimelineRequest;
import com.vmfg.project.request.UpdateDesignHdrRequest;
import com.vmfg.project.request.WbsIDRequest;
import com.vmfg.project.response.getelementHdrDistinctResponse;
import com.vmfg.sales.dao.impl.UploadManagementDAO;
import com.vmfg.task.entity.GetTaskEntryDtlEntity;
import com.vmfg.task.rowmapper.GetTaskEntryDtlRowMapper;
import com.vmfg.util.CommonMethod;
import com.vmfg.util.CommonNotifyMethod;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class ProjectDAO implements IProjectDAO {
	private static final Logger logger = LoggerFactory.getLogger(ProjectDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DesignDAO designDao;

	@Autowired
	private UploadManagementDAO uploadManagementDAO;
	
	@Autowired
	CommonNotifyMethod commonNotifyMethod;

	@Override
	public List<ProjectHdr> getProjectDtl(String tenantId, String custName, String fromDate, String toDate,
			String projectID, String empId, String pmId) {

		List<ProjectHdr> projHdr = null;
		String dateDiff = "";
		if(!fromDate.equalsIgnoreCase("")) {
			dateDiff = " AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		try {
			if (projectID.isEmpty()) {
				String getQ = "SELECT      @a:=@a+1 serial_number, eqh.ENQUIRY_CODE,     PM_HDR_ID,\r\n" + 
						"						    hdr.PROJECT_CODE,     hdr.PROJECT_NAME,     hdr.PROJECT_DESCRIPTION,\r\n" + 
						"						    ENQUIRY_ID,     hdr.PRODUCT_DETAILS,     hdr.CREATED_DATE,\r\n" + 
						"						    hdr.DUE_DATE,     hdr.CUSTOMER_NAME,     pms.STG_CODE,\r\n" + 
						"						    dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,eqh.PROJECT_HANDOVER_DATE AS PROJECT_HANDOVER_DATE,\r\n" + 
						"						    stg.STG_DESC,hdr.INITIATION_DATE,hdr.PLANNED_START_DATE,hdr.PLANNED_END_DATE,hdr.INDUSTRIAL_TYPE,hdr.SCOPE_OF_WORK,hdr.PRIORITY, \r\n" + 
//						"						    CASE \r\n" + 
//						"						        WHEN COUNT(shdr.MASTER_ID) > 0 THEN  (shdr.TOTAL_BUDGET_COST + shdr.CR_COST)   \r\n" + 
//						"						        ELSE 0 \r\n" + 
						"						    (shdr.TOTAL_BUDGET_COST + shdr.CR_COST) AS FINAL_SALE_VALUE, shdr.SB_HDR_ID AS SB_HDR_ID, eqh.IS_INTERNAL \r\n" + 
						"						FROM     project_hdr hdr,     process_config pms,\r\n" + 
						"						    document_status_type_code dst,     sales_enq_hdr eqh,\r\n" + 
						"						    process_assigned_team pat,  sales_budget_sheet_hdr shdr, stg_master stg,(SELECT @a:= 0) AS a  WHERE\r\n" + 
						"						    hdr.TRANSACTION_STATUS = dst.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
						"						        AND hdr.TRANSACTION_STAGE = pms.STG_CODE \r\n" + 
						"						        AND shdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" + 
						"						        and stg.STG_CODE = pms.STG_CODE AND pat.TENANT_ID = hdr.TENANT_ID AND pms.TENANT_ID = eqh.TENANT_ID \r\n" + 
						"						        AND pat.MASTER_ID = hdr.PM_HDR_ID         AND pat.ASSIGNED_EMP_ID = ?\r\n" + 
						"						        AND eqh.SE_ID = hdr.ENQUIRY_ID         AND hdr.CUSTOMER_NAME LIKE ?\r\n" + 
						"						                 AND hdr.TENANT_ID = ?\r\n" + 
						"						        AND pat.PM_ID = ?  AND pat.IS_ACTIVE = 1 " + dateDiff +" ";
				projHdr = this.jdbcTemplate.query(getQ, new ProjectHdrRowMapper(), empId, "%" + custName + "%",
						 tenantId, pmId);
			} else {
				String getQ = "SELECT \r\n" + " @a:=@a+1 serial_number,   eqh.ENQUIRY_CODE,\r\n" + "    PM_HDR_ID,\r\n"
						+ "    hdr.PROJECT_CODE,\r\n" + "    hdr.PROJECT_NAME,\r\n" + "    hdr.PROJECT_DESCRIPTION,\r\n"
						+ "    ENQUIRY_ID,\r\n" + "    hdr.PRODUCT_DETAILS,\r\n" + "    hdr.CREATED_DATE,\r\n"
						+ "    hdr.DUE_DATE,\r\n" + "    hdr.CUSTOMER_NAME,\r\n" + "    pms.STG_COM_DESC,\r\n"
						+ "    stg.STG_DESC,\r\n" + "    dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
						+ "    hdr.INITIATION_DATE,\r\n" + "    hdr.PLANNED_START_DATE,\r\n"
						+ "    hdr.PLANNED_END_DATE,\r\n" + "    hdr.INDUSTRIAL_TYPE,\r\n"
						+ "    hdr.SCOPE_OF_WORK,\r\n" + "    hdr.PRIORITY,\r\n" + "    CASE\r\n"
						+ "        WHEN COUNT(shdr.MASTER_ID) > 0 THEN (shdr.TOTAL_BUDGET_COST + shdr.CR_COST)  \r\n"
						+ "        ELSE 0\r\n" + "    END AS FINAL_SALE_VALUE,eqh.PROJECT_HANDOVER_DATE AS PROJECT_HANDOVER_DATE, shdr.SB_HDR_ID AS SB_HDR_ID , eqh.IS_INTERNAL\r\n" + "FROM\r\n"
						+ "    project_hdr hdr,\r\n" + "    process_config pms,\r\n"
						+ "    document_status_type_code dst,\r\n" + "    sales_enq_hdr eqh,\r\n"
						+ "    stg_master stg,\r\n" + "    sales_budget_sheet_hdr shdr,(SELECT @a:= 0) AS a\r\n" + "WHERE\r\n"
						+ "    hdr.TRANSACTION_STATUS = dst.DOCUMENT_STATUS_TYPE_CODE\r\n"
						+ "        AND hdr.TRANSACTION_STAGE = pms.STG_CODE\r\n"
						+ "        AND stg.STG_CODE = pms.STG_CODE\r\n" + "        AND eqh.SE_ID = hdr.ENQUIRY_ID\r\n"
						+ "        AND shdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" + "        AND PM_HDR_ID = ?\r\n"
						+ "        AND hdr.TENANT_ID = ?\r\n" + "ORDER BY hdr.DUE_DATE , hdr.PRIORITY;";
				projHdr = this.jdbcTemplate.query(getQ, new ProjectHdrRowMapper(), projectID, tenantId);
			}

		} catch (Exception ex) {
			logger.error("getProjectDtl error " + ex.getMessage());
		}
		return projHdr;
	}

	@Override
	public List<ProjectWBSTemplate> getWbsTemplate(TenantRequest tenantReq) {
		List<ProjectWBSTemplate> getProjWBS = null;

		try {

			String getQ = "select PWB_CODE as PM_TEMP_ID,PWB_DESC as TEMPLATE_NAME  from  project_wbs_template_mst where TENANT_ID=?";
			getProjWBS = this.jdbcTemplate.query(getQ, new ProjectWBSTempRowMapper(), tenantReq.getTenantID());

		} catch (Exception ex) {
			logger.error("getWbsTemplate error " + ex.getMessage());
		}
		return getProjWBS;
	}

	@Override
	public ProjectInternalResponse getIsInternalOrNot(String tenantId, String projectCode) {
		return null;
	}

	@Override
	public ProjectInternalResponse getProjectInternal(String tenantId, String pmHdrId) {
		String getQ = "SELECT se.IS_INTERNAL " +
				"FROM project_hdr ph, sales_enq_hdr se " +
				"WHERE ph.ENQUIRY_ID = se.SE_ID " +
				"AND ph.TENANT_ID = ? " +
				"AND ph.PM_HDR_ID = ?";   // ✅ changed PROJECT_CODE -> PM_HDR_ID

		logger.info("DAO - Running query: {} with tenantId={} and pmHdrId={}",
				getQ, tenantId, pmHdrId);

		List<ProjectInternalResponse> results = jdbcTemplate.query(
				getQ,
				new ProjectInternalRowMapper(),
				tenantId,
				pmHdrId
		);

		logger.info("DAO - Query result size: {}", results.size());
		if (!results.isEmpty()) {
			logger.info("DAO - First result: {}", results.get(0));
		}
		return results.isEmpty() ? null : results.get(0);
	}



	@Override
	public List<ProjectWBSTemplate> getWbsTemplateById(WbsIDRequest wbsReq) {
		List<ProjectWBSTemplate> getProjWBS = null;

		try {

			String getQ = "SELECT \r\n" + "    pwt.*, em.EMPLOYEE_FIRSTNAME,dt.DEPARTMENT_NAME\r\n" + "FROM\r\n"
					+ "    project_wbs_template pwt,\r\n" + "    employee_mst em,department dt\r\n" + "WHERE\r\n"
					+ "    pwt.TENANT_ID = ?\r\n" + "        AND TEMPLATE_NAME = ?\r\n"
					+ "        AND em.EMPLOYEE_ID = pwt.RESPONSIBLE_USER\r\n"
					+ "        and dt.DEPARTMENT_CODE = em.DEPARTMENT_CODE";
			getProjWBS = this.jdbcTemplate.query(getQ, new ProjectWBSTempRowMapper(), wbsReq.getTenantID(),
					wbsReq.getTempCode());

		} catch (Exception ex) {
			logger.error("getWbsTemplateById error " + ex.getMessage());
		}
		return getProjWBS;
	}

	@Override
	public int insertUpdateProjectMilestone(ProjectTimelineRequest projTimeReq) {
		int resp = 0;
		try {

			if (projTimeReq.getPtId() != null && !projTimeReq.getPtId().isEmpty()) {
				String checkQ = "select case when IS_INITIATED is null then 0  else CAST(IS_INITIATED AS UNSIGNED) end as IS_INITIATED from project_timeline where PT_ID= ? \r\n" 
				+ " and TENANT_ID= ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(checkQ,projTimeReq.getPtId(), projTimeReq.getTenantId());
				int initated = Integer.parseInt(resultMap.get("IS_INITIATED").toString());
				if (initated == 0) {
					String updateQ = "update project_timeline set \r\n"
							+ "PM_HDR_ID=?, PM_TEMP_ID=?, MILESTONE_NAME=?, RESPONSIBLE_NAME=?, \r\n"
							+ "RESPONSIBLE_DEPT_CODE=?, PLANNED_START_DATE=?, PLANNED_END_DATE=?, IS_INITIATED=?, LAST_UPDATED_DATETIME=?\r\n"
							+ "where PT_ID= ? and TENANT_ID=?";
					int qResp = this.jdbcTemplate.update(updateQ, projTimeReq.getPmHdrId(),
							projTimeReq.getPmTempId().isEmpty() ? 0 : projTimeReq.getPmTempId().isEmpty(),
									projTimeReq.getMilestoneName(), projTimeReq.getResponsibleName(),
									projTimeReq.getResponsibleDeptCode(), projTimeReq.getPlannedStartDate(),
									projTimeReq.getPlannedEndDate(), 0, CommonMethod.getCurrentDateTime(),
									projTimeReq.getPtId(), projTimeReq.getTenantId());
					if (qResp > 0) {
						resp = 200;
					} else {
						resp = 0;
					}

				} else {
					resp = 200;
				}

			} else {
				String insertQ = "INSERT INTO project_timeline (PM_HDR_ID, PM_TEMP_ID, MILESTONE_NAME, RESPONSIBLE_NAME, \r\n"
						+ "RESPONSIBLE_DEPT_CODE, PLANNED_START_DATE, PLANNED_END_DATE, IS_INITIATED, LAST_UPDATED_DATETIME, TENANT_ID) \r\n"
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				int qResp = this.jdbcTemplate.update(insertQ, projTimeReq.getPmHdrId(),
						projTimeReq.getPmTempId().isEmpty() ? 0 : projTimeReq.getPmTempId(),
								projTimeReq.getMilestoneName(), projTimeReq.getResponsibleName(),
								projTimeReq.getResponsibleDeptCode(), projTimeReq.getPlannedStartDate(),
								projTimeReq.getPlannedEndDate(), 0, CommonMethod.getCurrentDateTime(),
								projTimeReq.getTenantId());
				if (qResp > 0) {
					resp = 200;
				} else {
					resp = 0;
				}
			}

		} catch (Exception ex) {
			logger.error("insertUpdateProjectMilestone error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public ResponseAsMessage deleteWBSById(DeleteTimeWBSByIDRequest deleteById) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {
			String countCheckStr = "SELECT \n"
					+ "    COUNT(*) as COUNT\n"
					+ "FROM\n"
					+ "    project_timeline pt\n"
					+ "        INNER JOIN\n"
					+ "    task_entry_hdr teh ON pt.PM_HDR_ID = teh.PM_HDR_ID\n"
					+ "     \n"
					+ "WHERE\n"
					+ "    PT_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(countCheckStr,deleteById.getPtID());
			int countCheck = Integer.parseInt(resultMap.get("COUNT").toString());
			if(countCheck ==0) {
			String delQ = "delete from project_timeline where PT_ID= ? and TENANT_ID=?";
			this.jdbcTemplate.update(delQ, deleteById.getPtID(), deleteById.getTenantID());
			rm.setResponseCode(ResponseMessageMap.responseCodeOk);
			rm.setResponseMessage(ResponseMessageMap.successfulDeleted);
			}else {
				rm.setResponseCode(ResponseMessageMap.failToupdateCode);
				rm.setResponseMessage(ResponseMessageMap.taskcantdeleteMsg);	
			}
			
		} catch (Exception ex) {
			logger.error("deleteWBSById error " + ex.getMessage());
		}
		return rm;
	}

	@Override
	public List<ProjectTimelineResp> getTimeLineByPM(ProjectByIDRequest projHdr) {
		List<ProjectTimelineResp> resp = null;
		try {
			// this code tobe revisited after assy and other sections are done.

			String getQ = "SELECT \r\n" + "    pt.*,em.EMPLOYEE_FIRSTNAME ,dt.DEPARTMENT_NAME\r\n" + "FROM\r\n"
					+ "    project_timeline pt,\r\n" + "    employee_mst em,\r\n" + "    department dt\r\n"
					+ "WHERE\r\n" + "    pt.RESPONSIBLE_DEPT_CODE = dt.DEPARTMENT_CODE\r\n"
					+ "    and pt.RESPONSIBLE_NAME =em.EMPLOYEE_ID and pt.PM_HDR_ID = ? and pt.TENANT_ID=?";
			resp = this.jdbcTemplate.query(getQ, new ProjectTimelineRowMapper(), projHdr.getProjectID(),
					projHdr.getTenantID());

		} catch (Exception ex) {
			logger.error("getTimeLineByPM error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public int insertKeyAreaByPMId(KeyAreaRequest pt) {
		int resp = 0;
		try {

			String insertQ = "INSERT INTO project_key_area (PM_HDR_ID, PK_ID, TENANT_ID) \r\n" + "VALUES (?, ?, ?)";
			int qResp = this.jdbcTemplate.update(insertQ, pt.getPmHdrId(), pt.getPkId(), pt.getTenantId());
			if (qResp > 0) {
				resp = 200;
			} else {
				resp = 0;
			}
		} catch (Exception ex) {
			logger.error("insertKeyAreaByPMId error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public ResponseAsMessage deleteWBSById(KeyAreaDelRequest delReq) {
		ResponseAsMessage rm = new ResponseAsMessage();
		try {

			String checkQ = "select count(*) AS COUNT from indent_hdr where PROJECT_ID = ? \r\n" 
			+ " and PKA_ID= ? and TENANT_ID= ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(checkQ,delReq.getProjectid(), delReq.getPkaId(), delReq.getTenantId());
			int cnt = Integer.parseInt(resultMap.get("COUNT").toString());
			if (cnt > 0) {
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseMessage(ResponseMessageMap.indentLinkedtoKeyArea);
			} else {
				List<ProjectSubAreaExtnEntity> keyId = designDao.getProjectExtnByProjSubId(delReq.getPkaId());

				keyId.forEach(li -> {
					updateBudgetExtn(li.getSbExtnId(), li.getAllocatedQty(), li.getAllocateVal());
					deleteSubAreaExtn(li.getPkseId());

				});
				String delQ = "delete from project_key_area where PKA_ID=? and TENANT_ID=?";
				this.jdbcTemplate.update(delQ, delReq.getPkaId(), delReq.getTenantId());
				List<String> messageList = new ArrayList<>();
				List<String> otherEmp = new ArrayList<>();
				String projCode =getProjCodeByProjId(delReq.getProjectid(),delReq.getTenantId());
				
				messageList.add("Project "+projCode);
				commonNotifyMethod.InvokeNotificationMethod(2, 10, null, delReq.getTenantId(), messageList, otherEmp, "1",delReq.getPmId(), delReq.getProjectid(),null);
				rm.setResponseCode(ResponseMessageMap.responseCodeOk);
				rm.setResponseMessage(ResponseMessageMap.successfulDeleted);
			}
		} catch (Exception ex) {
			logger.error("deleteWBSById error " + ex.getMessage());
		}
		return rm;
	}
	@Override
	public String getProjCodeByProjId(String projectId, String tenantId) {
		String projectCode = "";
		try {
			String getCode = "select PROJECT_CODE from project_hdr where PM_HDR_ID= ? and TENANT_ID= ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getCode, projectId, tenantId);
		    projectCode = resultMap.get("PROJECT_CODE").toString();

		} catch (Exception ex) {
			logger.error("getProjectCodeByProjId method Error" + ex);
		}
		return projectCode;

	}
	@Override
	public List<KeyArea> getPKForProj(ProjectByIDRequest projHdr) {
		// TODO Auto-generated method stub

		List<KeyArea> ka = null;
		try {
			String getQ = "SELECT \r\n" + "    pk.PK_ID ,pk.PK_DESC\r\n" + "FROM\r\n" + "    project_key_area pka,\r\n"
					+ "    project_key_area pk\r\n" + "WHERE\r\n"
					+ "    pk.PK_ID = pka.PKA_ID and pka.PM_HDR_ID= ? and pka.TENANT_ID =?";
			ka = this.jdbcTemplate.query(getQ, new DesignAreaRowMapper(), projHdr.getProjectID(),
					projHdr.getTenantID());

		} catch (Exception ex) {
			logger.error("getPKForProj error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public int updateDesignIndentstart(String deHdrId, String tenantId) {
		int updateDtl = 0;
		try {
			String updateDtlStr = "update design_hdr set START_INDENT_REQUEST = case when START_INDENT_REQUEST > 0 then  0 else 1 end where DE_HDR_ID = ? "
					 + " and  TENANT_ID  = ?";
			updateDtl = this.jdbcTemplate.update(updateDtlStr, deHdrId, tenantId, int.class);

		} catch (Exception ex) {
			logger.error("updateDesignIndentstart Error" + ex);
		}
		return updateDtl;
	}

	@Override
	public getLinkStatusByPMIdRespEntity linkStatusCount(String pmHdrId, String pkaId, String tenantId) {
		List<getLinkStatusByPMIdRespEntity> ka = null;
		try {
			String getQ = "SELECT   \r\n" + "					    pks.PK_ID,  \r\n"
					+ "					    pks.PKA_ID,  \r\n" + "					    pks.PM_HDR_ID,  \r\n"
					+ "					    hdr.CUSTOMER_NAME,  \r\n" + "					    CASE  \r\n"
					+ "					        WHEN SUM(ext.ALLOCATED_VALUE) IS NOT NULL THEN 1  \r\n"
					+ "					        ELSE 0  \r\n" + "					    END AS TOTAL_COUNT,  \r\n"
					+ "					    CASE  \r\n"
					+ "					        WHEN SUM(ext.ALLOCATED_VALUE) IS NOT NULL THEN SUM(ext.ALLOCATED_VALUE)  \r\n"
					+ "					        ELSE 0  \r\n" + "					    END AS ALLOCATED,  \r\n"
					+ "					    mst.PK_DESC  \r\n" + "					FROM  \r\n"
					+ "					    project_key_area pks  \r\n" + "					        LEFT JOIN  \r\n"
					+ "					    project_key_area_extn ext ON pks.PKA_ID = ext.PKA_ID  \r\n"
					+ "					        INNER JOIN  \r\n"
					+ "					    project_key_area_mst mst ON mst.PK_ID = pks.PK_ID  \r\n"
					+ "					        INNER JOIN  \r\n"
					+ "					    project_hdr hdr ON hdr.PM_HDR_ID = pks.PM_HDR_ID  \r\n"
					+ "					WHERE  \r\n" + "					    pks.PM_HDR_ID = ?  \r\n"
					+ "					        AND pks.PK_ID =? \r\n"
					+ "					        AND pks.TENANT_ID = ? ";
			ka = this.jdbcTemplate.query(getQ, new getLinkStatusByPMIdRespRowMapper(), pmHdrId, pkaId, tenantId);

		} catch (Exception ex) {
			logger.error("linkStatusCount error " + ex.getMessage());
		}
		return ka.get(0);
	}

	@Override
	public List<getelementHdrDistinctResponse> getelementHdrDistinctList(String mstId, String keyCode,
			String tenantId) {
		List<getelementHdrDistinctResponse> ka = null;
		try {
			String getQ = "SELECT DISTINCT\r\n" + "    (extn.ELEMENT_HDR) As ELEMENT_DESC\r\n" + "FROM\r\n"
					+ "    sales_budget_sheet_hdr hdr\r\n" + "        INNER JOIN\r\n"
					+ "    sales_budget_sheet_dtl dtl ON hdr.SB_HDR_ID = dtl.SB_HDR_ID\r\n" + "        INNER JOIN\r\n"
					+ "    sales_budget_sheet_extn extn ON extn.SB_DTL_ID = dtl.SB_DTL_ID\r\n" + "WHERE\r\n"
					+ "    hdr.MASTER_ID = ? and hdr.TENANT_ID =? and dtl.KEY_CATEGORY= ? and extn.ELEMENT_HDR <> '' ";
			ka = this.jdbcTemplate.query(getQ, new ElementHdrDistinctRowMapper(), mstId, tenantId, keyCode);

		} catch (Exception ex) {
			logger.error("getelementHdrDistinctList error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public List<SubAreaPmHdrListEntity> getsubAreaPmHdrList(String pmHdrId, String pkaId, String tenantId) {
		List<SubAreaPmHdrListEntity> ka = null;
		try {
			String getQ = "SELECT \r\n" + "    ext.*,\r\n" + "    pks.PM_HDR_ID,\r\n" + "    hdr.CUSTOMER_NAME,\r\n"
					+ "    mst.PK_DESC,\r\n" + "    sextn.ELEMENT_HDR,\r\n" + "    sextn.ELEMENT_DTL,\r\n"
					+ "    sextn.SB_DTL_ID,\r\n" + "    sbc.SBC_CODE,\r\n" + "    sbc.SBC_DESC,\r\n"
					+ "    pks.TENANT_ID\r\n" + "FROM\r\n" + "    project_key_area_extn ext\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area pks ON pks.PKA_ID = ext.PKA_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_key_area_mst mst ON mst.PK_ID = pks.PK_ID\r\n"
					+ "        INNER JOIN\r\n" + "    project_hdr hdr ON hdr.PM_HDR_ID = pks.PM_HDR_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    sales_budget_sheet_extn sextn ON sextn.SB_EXTN_ID = ext.SB_EXTN_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    sales_budget_sheet_dtl sdtl ON sdtl.SB_DTL_ID = sextn.SB_DTL_ID\r\n"
					+ "        INNER JOIN\r\n" + "    sales_budget_category sbc ON sbc.SBC_CODE = sdtl.KEY_CATEGORY\r\n"
					+ "WHERE\r\n" + "    pks.PM_HDR_ID = ? AND pks.PKA_ID =? \r\n" + "        AND pks.TENANT_ID = ? ";
			ka = this.jdbcTemplate.query(getQ, new SubAreaPmHdrListRowMapper(), pmHdrId, pkaId, tenantId);

		} catch (Exception ex) {
			logger.error("getsubAreaPmHdrList error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public List<SalesBudgetExtnDtlEntity> getsalesBudgetExtnDtl(String pmHdrId, String pskId, String tenantId,
			String keyCode) {
		List<SalesBudgetExtnDtlEntity> ka = null;
		try {
			String getQ = "SELECT \r\n" + "    extn.ELEMENT_HDR,\r\n"
					+ "    extn.TOTAL_VALUE - extn.ALLOCATED_VALUE AS ALLOCATED_VALUE,\r\n"
					+ "    extn.QTY - extn.ALLOCATED_QTY AS ALLOCATED_QTY,\r\n" + "    extn.ELEMENT_HDR,\r\n"
					+ "    extn.ELEMENT_DTL,\r\n" + "    extn.SB_DTL_ID,\r\n" + "    extn.SPECIFICATION,\r\n"
					+ "    extn.MAKE,extn.SB_EXTN_ID,\r\n" + "    sbc.SBC_CODE,\r\n" + "    sbc.SBC_DESC,\r\n"
					+ "    hdr.TENANT_ID,\r\n" + "    extn.TOTAL_VALUE / extn.QTY AS PER_PART_VAUE\r\n" + "FROM\r\n"
					+ "    sales_budget_sheet_hdr hdr,\r\n" + "    sales_budget_sheet_dtl dtl,\r\n"
					+ "    sales_budget_sheet_extn extn,\r\n" + "    sales_budget_category sbc\r\n" + "WHERE\r\n"
					+ "    hdr.SB_HDR_ID = dtl.SB_HDR_ID\r\n" + "        AND dtl.SB_DTL_ID = extn.SB_DTL_ID\r\n"
					+ "        AND sbc.SBC_CODE = dtl.KEY_CATEGORY\r\n" + "        AND hdr.MASTER_ID = ? \r\n"
					+ "        AND extn.ELEMENT_HDR = ? \r\n"
					+ "        AND hdr.TENANT_ID = ? AND sbc.SBC_CODE= ? And extn.QTY - extn.ALLOCATED_QTY >0 AND dtl.TENANT_ID = ?";
			ka = this.jdbcTemplate.query(getQ, new SalesBudgetExtnDtlRowMapper(), pmHdrId, pskId, tenantId, keyCode, tenantId);

		} catch (Exception ex) {
			logger.error("getsalesBudgetExtnDtl error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public int deleteSubAreaExtn(String pkseId) {
		int qResp = 0;
		try {

			String deleteStr = "DELETE FROM `project_key_area_extn` WHERE `PKSE_ID`=? ";
			qResp = this.jdbcTemplate.update(deleteStr, pkseId);

		} catch (Exception ex) {
			logger.error("deleteSubAreaExtn error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int insertsubAreaExtn(String pkaId, String sbExtnId, String allocatedQty, String allocatedValue) {
	    int qResp = 0;
	    try {
	        String countSubAreaExtnStr = "select Count(*) AS COUNT from project_key_area_extn where PKA_ID = ? and SB_EXTN_ID = ?";
	        Map<String, Object> resultMap = jdbcTemplate.queryForMap(countSubAreaExtnStr, pkaId, sbExtnId);
	        int countSubAreaExtn = Integer.parseInt(resultMap.get("COUNT").toString());

	        
	        BigDecimal allocQty = new BigDecimal(allocatedQty).setScale(4, RoundingMode.HALF_UP);
	        BigDecimal allocValue = new BigDecimal(allocatedValue).setScale(4, RoundingMode.HALF_UP);

	        if (countSubAreaExtn > 0) {
	            String updateStr = "UPDATE project_key_area_extn SET ALLOCATED_QTY = ALLOCATED_QTY + ?, ALLOCATED_VALUE = ALLOCATED_VALUE + ? WHERE PKA_ID = ? AND SB_EXTN_ID = ?";
	            qResp = jdbcTemplate.update(updateStr, allocQty, allocValue, pkaId, sbExtnId);
	        } else {
	            String insertStr = "INSERT INTO project_key_area_extn (PKA_ID, SB_EXTN_ID, ALLOCATED_QTY, ALLOCATED_VALUE) VALUES (?, ?, ?, ?)";
	            qResp = jdbcTemplate.update(insertStr, pkaId, sbExtnId, allocQty, allocValue);
	        }
	    } catch (Exception ex) {
	        logger.error("insertsubAreaExtn error " + ex.getMessage());
	    }
	    return qResp;
	}

	@Override
	public int insertAreaExtn(String pkaId, String sbExtnId, String allocatedQty, String allocatedValue) {
		int qResp = 0;
		try {
			String countSubAreaExtnStr = "select Count(*) AS COUNT from project_key_area_extn where PKA_ID = ? " 
					+ " and SB_EXTN_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(countSubAreaExtnStr, pkaId, sbExtnId);
			int countSubAreaExtn = Integer.parseInt(resultMap.get("COUNT").toString());			
			if (countSubAreaExtn > 0) {
				String updateStr = "UPDATE `project_key_area_extn` SET `ALLOCATED_QTY`=ALLOCATED_QTY + ?,"  
						+ " `ALLOCATED_VALUE`=ALLOCATED_VALUE + ?"  
						+ " WHERE `PKA_ID`=?  and SB_EXTN_ID = ? ";
				qResp = this.jdbcTemplate.update(updateStr, allocatedQty, allocatedValue, pkaId, sbExtnId);
			} else {
				String insertStr = "INSERT INTO `project_key_area_extn` (`PKA_ID`, `SB_EXTN_ID`, `ALLOCATED_QTY`, `ALLOCATED_VALUE`) VALUES (?, ?,?, ?) ";
				qResp = this.jdbcTemplate.update(insertStr, pkaId, sbExtnId, allocatedQty, allocatedValue);
			}
		} catch (Exception ex) {
			logger.error("insertAreaExtn error " + ex.getMessage());
		}
		return qResp;

	}

	@Override
	public int updatesalesBudgetExtnval(String sbextnId, String qty, String value) {
		int qResp = 0;
		try {

			String updateStr = "update sales_budget_sheet_extn set ALLOCATED_QTY = ALLOCATED_QTY + ? , ALLOCATED_VALUE  = ALLOCATED_VALUE + ? where SB_EXTN_ID = ? ";
			qResp = this.jdbcTemplate.update(updateStr, qty, value, sbextnId);

		} catch (Exception ex) {
			logger.error("updatesalesBudgetExtnval error " + ex.getMessage());
		}
		return qResp;

	}

	@Override
	public List<SalesBudgetExtnListDtlEntity> getbugetextnListbyDSkId(String pmhdrId, String pskId, String tenantId) {
		List<SalesBudgetExtnListDtlEntity> ka = null;
		try {
			String getQ = "SELECT \r\n" + "    ext.DSK_ID,\r\n" + "    ext.PKSE_ID,\r\n" + "    ext.SB_EXTN_ID,\r\n"
					+ "   ABS( ext.ALLOCATED_VALUE -ext.BUDGET_VALUE) AS ALLOCATED_VALUE,\r\n"
					+ "   ABS( ext.ALLOCATED_QTY - ext.BUDGET_QTY) AS ALLOCATED_QTY,\r\n"
					+ "    ext.ALLOCATED_VALUE / ext.ALLOCATED_QTY AS PER_PART_VAUE, pks.PM_HDR_ID,\r\n"
					+ "    hdr.CUSTOMER_NAME,\r\n" + "    mst.PSK_DESC,\r\n" + "    sextn.ELEMENT_HDR,\r\n"
					+ "    sextn.ELEMENT_DTL,\r\n" + "    sextn.SB_DTL_ID,sextn.SPECIFICATION,sextn.MAKE,\r\n"
					+ "    sbc.SBC_CODE,\r\n" + "    sbc.SBC_DESC,\r\n" + "    pks.TENANT_ID\r\n" + "FROM\r\n"
					+ "    project_key_sub_area_extn ext\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_sub_area pks ON pks.DSK_ID = ext.DSK_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_sub_area_mst mst ON mst.PSK_ID = pks.PSK_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_hdr hdr ON hdr.PM_HDR_ID = pks.PM_HDR_ID\r\n" + "        INNER JOIN\r\n"
					+ "    sales_budget_sheet_extn sextn ON sextn.SB_EXTN_ID = ext.SB_EXTN_ID\r\n"
					+ "        INNER JOIN\r\n"
					+ "    sales_budget_sheet_dtl sdtl ON sdtl.SB_DTL_ID = sextn.SB_DTL_ID\r\n"
					+ "        INNER JOIN\r\n" + "    sales_budget_category sbc ON sbc.SBC_CODE = sdtl.KEY_CATEGORY\r\n"
					+ "WHERE\r\n" + "  pks.PM_HDR_ID = ? AND ext.DSK_ID = ? \r\n"
					+ "        AND pks.TENANT_ID = ? Group by ext.SB_EXTN_ID";
			ka = this.jdbcTemplate.query(getQ, new SalesBudgetExtnDtlListRowMapper(), pmhdrId, pskId, tenantId);

		} catch (Exception ex) {
			logger.error("getbugetextnListbyDSkId error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public List<IndentBudgetDtlEntity> getindentBudgetDtlList(String pskId, String pkseId, String tenantId) {
		List<IndentBudgetDtlEntity> ka = null;
		try {
			String getQ = "select * from indent_budget_dtl where PSK_ID = ?  ";
			ka = this.jdbcTemplate.query(getQ, new IndentBudgetDtlRowMapper(), pskId);

		} catch (Exception ex) {
			logger.error("getindentBudgetDtlList error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public List<ProjectSubAreaExtnEntity> getProjectSubAreaExtnRowMapper(String pkseId) {
		List<ProjectSubAreaExtnEntity> ka = null;
		try {
			String getQ = "select * from project_key_area_extn where PKSE_ID = ? ";
			ka = this.jdbcTemplate.query(getQ, new ProjectSubAreaExtnRowMapper(), pkseId);

		} catch (Exception ex) {
			logger.error("getProjectSubAreaExtnRowMapper error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public int insertindentBudgetDtl(String indentDtl, String pkseId, String qty, String value) {
		int qResp = 0;
		try {
			// String indentDtlCountStr = "select count(*) from indent_budget_dtl where
			// INDENT_DTL_ID = '" + indentDtl
			// + "' and PKSE_ID = '" + pkseId + "'";
			// int indentDtlCount = this.jdbcTemplate.queryForObject(indentDtlCountStr,
			// int.class);
			// if (indentDtlCount > 0) {
			// String updateStr = "update indent_budget_dtl set ALLOCATED_QTY =ALLOCATED_QTY
			// + ? , ALLOCATED_VALUE =ALLOCATED_VALUE+ ? where PKSE_ID = ? and
			// INDENT_DTL_ID=?";
			// qResp = this.jdbcTemplate.update(updateStr, qty, value, pkseId, indentDtl);
			// } else {
			// String insertStr = "INSERT INTO `indent_budget_dtl` (`INDENT_DTL_ID`,
			// `PKSE_ID`, `ALLOCATED_QTY`, `ALLOCATED_VALUE`) VALUES (?,?,?,?)";
			// qResp = this.jdbcTemplate.update(insertStr, indentDtl, pkseId, qty, value);
			//
			// }
			//
			// String upQ = "UPDATE project_key_sub_area_extn SET BUDGET_QTY=BUDGET_QTY+?,
			// BUDGET_VALUE=BUDGET_VALUE+? WHERE PKSE_ID=?";
			// this.jdbcTemplate.update(upQ, qty, value, pkseId);
			// String indentHdrId = getindentHdrId(indentDtl);
			// String budgetVal = getindentBudgetDtlsByIndentId(indentHdrId);
			// indentUploadDAO.updateBudgetDtl(budgetVal, indentHdrId);
		} catch (Exception ex) {
			logger.error("insertindentBudgetDtl error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public String getindentHdrId(String indentDtlId) {
		String getindentHdrId = "";
		try {
			String getQ = "	select INDENT_ID from indent_dtl where INDENT_DTL_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ, indentDtlId);
			getindentHdrId = resultMap.get("INDENT_ID").toString();			
			} catch (Exception ex) {
			logger.error("getindentHdrId error " + ex.getMessage());
		}
		return getindentHdrId;
	}

	public String getindentBudgetDtlsByIndentId(String indentId) {
		String list = null;
		try {
			String getQ = "SELECT \r\n"
					+ " case when Sum(ALLOCATED_VALUE) is not null then   sum(ALLOCATED_VALUE) else 0 end as allocatedValue \r\n"
					+ "FROM\r\n" + "    indent_budget_dtl ibd\r\n" + "        INNER JOIN\r\n"
					+ "    indent_dtl id ON ibd.INDENT_DTL_ID = id.INDENT_DTL_ID\r\n" + "WHERE\r\n"
					+ "    id.INDENT_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ, indentId);
			list = resultMap.get("INDENT_ID").toString();	
		} catch (Exception ex) {
			logger.error("getindentBudgetDtlsByIndentId error " + ex.getMessage());
		}
		return list;
	}

	@Override
	public BigDecimal getTotalSubExtnVal(String pkId, String pmHdrId, String tenantId) {
		BigDecimal qResp = BigDecimal.ZERO;
		try {
			String valSumStr = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN COUNT(*) > 0 THEN SUM(ext.ALLOCATED_VALUE)\r\n" + "        ELSE 0\r\n"
					+ "    END sum\r\n" + "FROM\r\n" + "    project_key_area mst\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_area_extn ext ON mst.PKA_ID = ext.PKA_ID\r\n" + "WHERE\r\n"
					+ "    mst.PM_HDR_ID =? AND ext.PKA_ID = ? and mst.TENANT_ID= ? ";
					
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(valSumStr, pmHdrId, pkId, tenantId);
			qResp = new BigDecimal(resultMap.get("sum").toString());
		} catch (Exception ex) {
			logger.error("getTotalSubExtnVal error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public String getmstIdByPmHdrId(String pmHdrId, String tenantId) {
		String qResp = "";
		try {
			String mstIdStr = "SELECT \r\n" + "    CASE\r\n" + "        WHEN COUNT(*) > 0 THEN ENQUIRY_ID\r\n"
					+ "        ELSE ''\r\n" + "    END AS ENQUIRY_ID\r\n" + "FROM\r\n" + "    project_hdr\r\n"
					+ "WHERE\r\n" + "    PM_HDR_ID = ? AND TENANT_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(mstIdStr, pmHdrId, tenantId);
			qResp = resultMap.get("ENQUIRY_ID").toString();

		} catch (Exception ex) {
			logger.error("getmstIdByPmHdrId error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int updateBudgetExtn(String sbExtnId, String qty, String value) {
		int qResp = 0;
		try {

			String updateStr = "UPDATE sales_budget_sheet_extn \r\n" + "SET \r\n"
					+ "    ALLOCATED_QTY = ALLOCATED_QTY - ?,\r\n"
					+ "    ALLOCATED_VALUE = ALLOCATED_VALUE - ?\r\n" + "WHERE\r\n" + "    SB_EXTN_ID = ? ";
			qResp = this.jdbcTemplate.update(updateStr, qty, value, sbExtnId);

		} catch (Exception ex) {
			logger.error("updateBudgetExtn error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int countIndentBudgetCount(String pkseId) {
		int qResp = 0;
		try {

			String countStr = "select count(*) as count from indent_budget_dtl where PKSE_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(countStr, pkseId);
			qResp = Integer.parseInt(resultMap.get("count").toString());

		} catch (Exception ex) {
			logger.error("countIndentBudgetCount error " + ex.getMessage());
		}
		return qResp;

	}

	@Override
	public List<GetindentbudgetDtlEntity> getindentbudgetDtl(String indentDtlId) {
		List<GetindentbudgetDtlEntity> ka = null;
		try {
			String getQ = "SELECT \r\n"
					+ "    dtl.INDENT_BUD_ID,dtl.ALLOCATED_VALUE,dtl.ALLOCATED_QTY,dtl.INDENT_DTL_ID,sbs.ELEMENT_HDR,sbs.ELEMENT_DTL,pkm.PSK_DESC\r\n"
					+ "FROM\r\n" + "    indent_budget_dtl dtl\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_sub_area_extn ext ON dtl.PKSE_ID = ext.PKSE_ID\r\n" + "        INNER JOIN\r\n"
					+ "    project_key_sub_area pka ON pka.DSK_ID = ext.DSK_ID\r\n" + "		INNER JOIN\r\n"
					+ "	project_key_sub_area_mst pkm ON pkm.PSK_ID = pka.PSK_ID\r\n" + "		INNER JOIN\r\n"
					+ "	sales_budget_sheet_extn sbs on sbs.SB_EXTN_ID = ext.SB_EXTN_ID where dtl.INDENT_DTL_ID = ?";
			ka = this.jdbcTemplate.query(getQ, new GetindentbudgetDtlRowMapper(), indentDtlId);

		} catch (Exception ex) {
			logger.error("getindentbudgetDtl error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public List<IndentBudgetDtlEntity> getindentBudgetDtlById(String indentBudId) {
		List<IndentBudgetDtlEntity> ka = null;
		try {
			String getQ = "select * from indent_budget_dtl where INDENT_BUD_ID = ?  ";
			ka = this.jdbcTemplate.query(getQ, new IndentBudgetDtlRowMapper(), indentBudId);

		} catch (Exception ex) {
			logger.error("getindentBudgetDtlById error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public int updateBudgetQtyAndval(String pkseId, String qty, String val) {
		int qResp = 0;
		try {

			String updateStr = "UPDATE project_key_area_extn \r\n" + "SET \r\n" + "    BUDGET_QTY = BUDGET_QTY - ? ,"
				    + "    BUDGET_VALUE = BUDGET_VALUE - ? \r\n" + "WHERE\r\n"
					+ "    PKSE_ID = ? ";
			qResp = this.jdbcTemplate.update(updateStr, qty, val, pkseId);
			String allocatedValUpdate = "update project_key_area_extn dtl inner join project_key_area_extn chk on chk.PKSE_ID = dtl.PKSE_ID  set dtl.BUDGET_VALUE = 0 where dtl.BUDGET_VALUE <0";
			this.jdbcTemplate.update(allocatedValUpdate);
			String allocatedQtyUpdate = "update project_key_area_extn dtl inner join project_key_area_extn chk on chk.PKSE_ID = dtl.PKSE_ID  set dtl.BUDGET_QTY = 0 where dtl.BUDGET_QTY <0";
			this.jdbcTemplate.update(allocatedQtyUpdate);
		} catch (Exception ex) {
			logger.error("updateBudgetQtyAndval error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int deleteindentBudGetId(String indentId) {
		int qResp = 0;
		try {
			// String indentHdrId = getIndentHdrIdByIndentBudId(indentId);
			 String deleteStr = "DELETE FROM `indent_budget_dtl` WHERE `INDENT_BUD_ID`=? ";
			 qResp = this.jdbcTemplate.update(deleteStr, indentId);
			//
			// String budgetVal = getindentBudgetDtlsByIndentId(indentHdrId);
			//
			// indentUploadDAO.updateBudgetDtl(budgetVal, indentHdrId);
		} catch (Exception ex) {
			logger.error("deleteindentBudGetId error " + ex.getMessage());
		}
		return qResp;
	}

	public String getIndentHdrIdByIndentBudId(String indentBugId) {
		String qResp = "";
		try {

			String getIndentHdrIdStr = "SELECT \r\n" + "    hdr.INDENT_ID as INDENT_ID \r\n" + "FROM\r\n" + "    indent_hdr hdr,\r\n"
					+ "    indent_dtl dtl,\r\n" + "    indent_budget_dtl bdtl\r\n" + "WHERE\r\n"
					+ "    hdr.INDENT_ID = dtl.INDENT_ID\r\n" + "        AND dtl.INDENT_DTL_ID = bdtl.INDENT_DTL_ID\r\n"
					+ "        AND bdtl.INDENT_BUD_ID = ?  ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getIndentHdrIdStr,indentBugId);
			qResp = resultMap.get("INDENT_ID").toString();

		} catch (Exception ex) {
			logger.error("getIndentHdrIdByIndentBudId error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int projectKeyAreaCount(String pmHdrId, String pkId, String tenantId) {
		int qResp = 0;
		try {

			String projectKeyCount = "select  count(*) as COUNT from project_key_area where PM_HDR_ID = ?" 
					+ " and PK_ID = ? and TENANT_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(projectKeyCount,pmHdrId, pkId, tenantId);
			qResp = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("projectKeyAreaCount error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int projectKeySubAreaCount(String pmHdrId, String pskId, String tenantId, String pkaId) {
		int qResp = 0;
		try {

			String projectKeySubCount = "select  count(*) AS COUNT from project_key_sub_area where PM_HDR_ID = ? \r\n"  
					+ " and PSK_ID = ? and TENANT_ID = ? AND PKA_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(projectKeySubCount, pmHdrId, pskId, tenantId, pkaId);
			qResp = Integer.parseInt(resultMap.get("COUNT").toString());

		} catch (Exception ex) {
			logger.error("projectKeySubAreaCount error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public String getBudgetValue(String projectID, String tenantID) {
		String budValu = "";
		try {

			String projectKeySubCount = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN SUM(BUDGET_VALUE) IS NULL THEN 0\r\n" + "        ELSE SUM(BUDGET_VALUE)\r\n"
					+ "    END as BUDGET_VALUE\r\n" + "FROM\r\n" + "    indent_hdr where PROJECT_ID =  ? "  
					+ " and TENANT_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(projectKeySubCount,projectID, tenantID);
			budValu = resultMap.get("BUDGET_VALUE").toString();
		} catch (Exception ex) {
			logger.error("getBudgetValue error " + ex.getMessage());
		}
		return budValu;
	}

	@Override
	public String getAllocValue(String projectID, String tenantID) {
		String allocValu = "";
		try {

			String projectKeySubCount = "SELECT \r\n" + "    CASE\r\n"
					+ "        WHEN SUM(SCM_BUDGET_ALLOCATED) IS NULL THEN 0\r\n"
					+ "        ELSE SUM(SCM_BUDGET_ALLOCATED)\r\n" + "    END as BUDGET_VALUE\r\n" + "FROM\r\n"
					+ "    indent_hdr where PROJECT_ID = ? and TENANT_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(projectKeySubCount, projectID, tenantID);
			allocValu = resultMap.get("BUDGET_VALUE").toString();

		} catch (Exception ex) {
			logger.error("getAllocValue error " + ex.getMessage());
		}
		return allocValu;
	}

	@Override
	public int getCountProjectKeyMst(String pkDesc, String tenantId,String pmHdrId) {
		int qResp = 0;
		try {

			String projectKeySubCount = "select  case when count(*) >0 then mst.PK_ID else 0 end As pkId from project_key_area_mst mst inner join project_key_area sub on sub.PK_ID = mst.PK_ID where mst.PK_DESC = ? and sub.PM_HDR_ID = ? "
					+ " and mst.TENANT_ID=?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(projectKeySubCount, pkDesc,pmHdrId, tenantId);
			qResp = Integer.parseInt(resultMap.get("pkId").toString());

		} catch (Exception ex) {
			logger.error("getCountProjectKeyMst error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int getCountProjectKeySubMst(String pskDesc, String tenantId, String pmHdrId) {
		int qResp = 0;
		try {

			String projectKeySubCount = "select  case when count(*) >0 then mst.PSK_ID else 0 end As PSK_ID from project_key_sub_area_mst mst inner join project_key_sub_area sub on sub.PSK_ID = mst.PSK_ID where mst.PSK_DESC = ? and sub.PM_HDR_ID = ? "
					+ " and mst.TENANT_ID= ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(projectKeySubCount, pskDesc,pmHdrId, tenantId);
			qResp = Integer.parseInt(resultMap.get("PSK_ID").toString());

		} catch (Exception ex) {
			logger.error("getCountProjectKeySubMst error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public String getLastKeyCode(String tenantId, String pmHdrId) {
		String lastCodeValu = "";
		try {
			String firstCodeStr = "select  count(*) AS Count from project_key_area_mst mst inner join project_key_area sub on sub.PK_ID = mst.PK_ID where sub.PM_HDR_ID = ? and mst.TENANT_ID=? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(firstCodeStr,pmHdrId,tenantId);
			int firstCode = Integer.parseInt(resultMap.get("Count").toString());
			if (firstCode > 0) {
				String lastCodeValuStr = "select  CODE from project_key_area_mst mst inner join project_key_area sub on sub.PK_ID = mst.PK_ID where sub.PM_HDR_ID = ? and mst.TENANT_ID=? order by mst.PK_ID desc limit 1";
				Map<String,Object> resultData = jdbcTemplate.queryForMap(lastCodeValuStr,pmHdrId,tenantId);
				lastCodeValu = resultData.get("CODE").toString();		
				} else {
				lastCodeValu = "0";
			}

		} catch (Exception ex) {
			logger.error("getLastCode error " + ex.getMessage());
		}
		return lastCodeValu;
	}

	@Override
	public String getLastKeySubCode(String tenantId, String pmHdrId) {
		String lastCodeValu = "";
		try {
			String firstCodeStr = "select  count(*) as Count from project_key_sub_area_mst mst inner join project_key_sub_area sub on sub.PSK_ID = mst.PSK_ID where sub.PM_HDR_ID = ? and mst.TENANT_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(firstCodeStr,pmHdrId,tenantId);
			int firstCode = Integer.parseInt(resultMap.get("Count").toString());
			if (firstCode > 0) {
				String lastCodeValuStr = "select mst.CODE from project_key_sub_area_mst mst inner join project_key_sub_area sub on sub.PSK_ID = mst.PSK_ID where sub.PM_HDR_ID = ? and mst.TENANT_ID = ? order by mst.PSK_ID desc limit 1";
				Map<String,Object> resultData = jdbcTemplate.queryForMap(lastCodeValuStr,pmHdrId,tenantId);
				lastCodeValu = resultData.get("CODE").toString();
			} else {
				lastCodeValu = "0";
			}

		} catch (Exception ex) {
			logger.error("getLastKeySubCode error " + ex.getMessage());
		}
		return lastCodeValu;
	}

	@Override
	public int insertProjectKeyArea(String Code, String pkDesc, String isActive, String tenantId) {
		int qResp = 0;
		try {

			String projectKeySubCount = "INSERT INTO `project_key_area_mst` (`CODE`, `PK_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?, ?,?,?)";
			// qResp = this.jdbcTemplate.update(projectKeySubCount,
			// Code,pkDesc,isActive,tenantId);
			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(projectKeySubCount, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, Code);
					ps.setString(2, pkDesc);
					ps.setString(3, isActive);
					ps.setString(4, tenantId);

					return ps;
				}

			}, holder);
			qResp = holder.getKey().intValue();
		} catch (Exception ex) {
			logger.error("insertProjectKeyArea error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int insertProjectKeySubArea(String Code, String pskDesc, String isActive, String tenantId) {
		int qResp = 0;
		try {

			String projectKeySubCount = "INSERT INTO `project_key_sub_area_mst` (`CODE`, `PSK_DESC`, `IS_ACTIVE`, `TENANT_ID`) VALUES (?, ?,?,?)";
			// qResp = this.jdbcTemplate.update(projectKeySubCount,
			// Code,pkDesc,isActive,tenantId);
			KeyHolder holder = new GeneratedKeyHolder();

			this.jdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(projectKeySubCount, Statement.RETURN_GENERATED_KEYS);

					ps.setString(1, Code);
					ps.setString(2, pskDesc);
					ps.setString(3, isActive);
					ps.setString(4, tenantId);

					return ps;
				}

			}, holder);
			qResp = holder.getKey().intValue();
		} catch (Exception ex) {
			logger.error("insertProjectKeySubArea error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int updateAllocatedAndBudgetVal(String allocatedVal, String budgetVal, String pkaId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE project_key_area SET ALLOCATED_VALUE=? ,BUDGET_VALUE =? WHERE PKA_ID=?;";
			updateStatus = this.jdbcTemplate.update(qry, allocatedVal,budgetVal, pkaId);
		} catch (Exception ex) {
			logger.error("updateAllocatedAndBudgetVal error " + ex.getMessage());
		}
		return updateStatus;
 
	}
	public int updateBudgetVal( String budgetVal, String pkaId) {
		int updateStatus = 0;
		try {
			String qry = "UPDATE project_key_area SET BUDGET_VALUE =? WHERE PKA_ID=?;";
			updateStatus = this.jdbcTemplate.update(qry,budgetVal, pkaId);
		} catch (Exception ex) {
			logger.error("updateBudgetVal error " + ex.getMessage());
		}
		return updateStatus;
 
	}

	@Override
	public String getAllocatedValSum(String pkaId) {
		String allocatedVal = "";
		try {
			String qry = "select case when count(*)>0 then SUM(ALLOCATED_VALUE) else 0 end as count from project_key_area_extn where PKA_ID= ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(qry, pkaId);
			allocatedVal = resultData.get("count").toString();
		} catch (Exception ex) {
			logger.error("getAllocatedValSum error " + ex.getMessage());
		}
		return allocatedVal;

	}

	@Override
	public String getAllocatedValSumByPmHdrId(String pmHdrId) {
		String allocatedVal = "0";
		try {
			String qry = "SELECT CASE WHEN COUNT(*)>0 THEN SUM(extn.ALLOCATED_VALUE) ELSE 0 END AS VAL " +
					"FROM project_key_area_extn extn " +
					"INNER JOIN project_key_area pka ON pka.PKA_ID = extn.PKA_ID " +
					"WHERE pka.PM_HDR_ID = ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(qry, pmHdrId);
			allocatedVal = resultData.get("VAL").toString();
		} catch (Exception ex) {
			logger.error("getAllocatedValSumByPmHdrId error " + ex.getMessage());
		}
		return allocatedVal;
	}

	@Override
	public String getUnallocatedSalesBudgetTotalByMstId(String mstId, String tenantId) {
		String unallocatedVal = "0";
		try {
			String qry = "SELECT CASE WHEN COUNT(*) > 0 THEN SUM(extn.TOTAL_VALUE - extn.ALLOCATED_VALUE) ELSE 0 END AS VAL " +
					"FROM sales_budget_sheet_extn extn " +
					"INNER JOIN sales_budget_sheet_dtl dtl ON dtl.SB_DTL_ID = extn.SB_DTL_ID " +
					"INNER JOIN sales_budget_sheet_hdr hdr ON hdr.SB_HDR_ID = dtl.SB_HDR_ID " +
					"WHERE hdr.MASTER_ID = ? AND hdr.TENANT_ID = ? AND extn.TOTAL_VALUE - extn.ALLOCATED_VALUE > 0";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, mstId, tenantId);
			unallocatedVal = resultMap.get("VAL").toString();
		} catch (Exception ex) {
			logger.error("getUnallocatedSalesBudgetTotalByMstId error " + ex.getMessage());
		}
		return unallocatedVal;
	}

	@Override
	public String getBudgetValSum(String pkaId) {
		String budgetVal = "";
		try {
			String qry = "select case when count(*)>0 then SUM(BUDGET_VALUE) else 0 end AS count from project_key_area_extn where PKA_ID= ?";
			Map<String,Object> resultData = jdbcTemplate.queryForMap(qry, pkaId);
			budgetVal = resultData.get("count").toString();		
			} catch (Exception ex) {
			logger.error("getBudgetValSum error " + ex.getMessage());
		}
		return budgetVal;
	}

	@Override
	public List<GetProjTimePlanDropDownEntity> getProjTimePlanDropDown(String tenantId) {
		List<GetProjTimePlanDropDownEntity> ka = null;
		try {
			String getQ = "SELECT \r\n" + "    PM_HDR_ID,\r\n"
					+ "    CONCAT(CUSTOMER_NAME, ' - ', PROJECT_CODE) AS PM_DESC,\r\n" + "    TENANT_ID\r\n"
					+ "FROM\r\n" + "    project_hdr\r\n" + "WHERE  TENANT_ID =? AND  \r\n"
					+ "    PROJECT_CODE IS NOT NULL\r\n" + "        AND CUSTOMER_NAME IS NOT NULL\r\n"
					+ "ORDER BY CREATED_DATE DESC\r\n" + "LIMIT 30 ";
			ka = this.jdbcTemplate.query(getQ, new GetProjTimePlanDropDownRowMapper(), tenantId);

		} catch (Exception ex) {
			logger.error("getProjTimePlanDropDown error " + ex.getMessage());
		}
		return ka;
	}

	@Override
	public List<ProjectTimelineResp> getTimeLineOrdByDate(String projHdrId, String tenantId) {
		List<ProjectTimelineResp> resp = null;
		try {
			// this code tobe revisited after assy and other sections are done.

			String getQ = "SELECT \r\n" + "    pt.*,em.EMPLOYEE_FIRSTNAME ,dt.DEPARTMENT_NAME\r\n" + "FROM\r\n"
					+ "    project_timeline pt,\r\n" + "    employee_mst em,\r\n" + "    department dt\r\n"
					+ "WHERE\r\n" + "    pt.RESPONSIBLE_DEPT_CODE = dt.DEPARTMENT_CODE\r\n"
					+ "    and pt.RESPONSIBLE_NAME =em.EMPLOYEE_ID and pt.PM_HDR_ID = ? and pt.TENANT_ID=? order by PLANNED_START_DATE";
			resp = this.jdbcTemplate.query(getQ, new ProjectTimelineRowMapper(), projHdrId, tenantId);

		} catch (Exception ex) {
			logger.error("getTimeLineByPM error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public BigDecimal getTotalallocatedVal(String pkaId) {
		BigDecimal qResp = BigDecimal.ZERO;
		try {
			String valSumStr = "select (ALLOCATED_VALUE - BUDGET_VALUE) as Value from project_key_area where PKA_ID= ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(valSumStr, pkaId);
			qResp = new BigDecimal(resultMap.get("Value").toString());
		} catch (Exception ex) {
			logger.error("getTotalallocatedVal error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public int updateProjPlanDate(String pmHdrId, String plannedDate, String endDate, String tenantId,
			String priority) {
		int qResp = 0;
		try {
			String valSumStr = "UPDATE `project_hdr` SET `PLANNED_START_DATE`=?, `PLANNED_END_DATE`=?,`PRIORITY`=? WHERE `PM_HDR_ID`=? AND `TENANT_ID` = ? ";
			qResp = this.jdbcTemplate.update(valSumStr, plannedDate, endDate, priority, pmHdrId, tenantId);
		} catch (Exception ex) {
			logger.error("updateProjPlanDate error " + ex.getMessage());
		}
		return qResp;
	}

	@Override
	public List<SumOfIndentHdrEntity> getSumOfIndentHdrEntity(String pkaId) {
		List<SumOfIndentHdrEntity> resp = null;
		try {

			String getQ = "SELECT \r\n" + "    CASE\r\n" + "        WHEN SUM(hdr.BUDGET_VALUE) IS NULL THEN 0\r\n"
					+ "        ELSE SUM(hdr.BUDGET_VALUE)\r\n" + "    END AS BUDGET_VALUE,\r\n" + "    CASE\r\n"
					+ "        WHEN SUM(hdr.SCM_BUDGET_ALLOCATED) IS NULL THEN 0\r\n"
					+ "        ELSE SUM(hdr.SCM_BUDGET_ALLOCATED)\r\n" + "    END AS SCM_BUDGET_ALLOCATED,\r\n"
					+ "    CASE\r\n" + "        WHEN SUM(hdr.TARGET_VALUE) IS NULL THEN 0\r\n"
					+ "        ELSE SUM(hdr.TARGET_VALUE)\r\n" + "    END AS TARGET_VALUE\r\n" + "FROM\r\n"
					+ "    indent_hdr hdr\r\n" + "WHERE\r\n" + "    PKA_ID = ? ";
			resp = this.jdbcTemplate.query(getQ, new SumOfIndentHdrRowMapper(), pkaId);

		} catch (Exception ex) {
			logger.error("getSumOfIndentHdrEntity error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public int updateDesignHdr(UpdateDesignHdrRequest designHdrRequest,List<String> messageList,List<String> otherEmpId) {

		int res = 0,returnType=0,returnMsg=0;
		try {
			if (designHdrRequest.getIsStatus().equalsIgnoreCase("0")) {
				// 0 means onLoad
				String Qry = "SELECT \r\n" + "    COUNT(*) as count \r\n" + "FROM\r\n" + "    design_hdr \r\n" + "WHERE\r\n"
						+ "    PM_HDR_ID = ? AND TENANT_ID = ?";
				Map<String,Object> resultData = jdbcTemplate.queryForMap(Qry, designHdrRequest.getPmHdrId(), designHdrRequest.getTenantId());
				int count = Integer.parseInt(resultData.get("count").toString());	
				if (count > 0) {
					String qrySelect = "SELECT CAST(START_INDENT_REQUEST AS UNSIGNED) as START_INDENT_REQUEST FROM design_hdr WHERE PM_HDR_ID=? AND TENANT_ID=?";
					Map<String,Object> resultMap = jdbcTemplate.queryForMap(qrySelect, designHdrRequest.getPmHdrId(),
							designHdrRequest.getTenantId());
					res = Integer.parseInt(resultMap.get("START_INDENT_REQUEST").toString());

				}

			} else {
				// 1 means onClick

				String Qry = "SELECT \r\n" + "    COUNT(*) as count\r\n" + "FROM\r\n" + "    design_hdr \r\n" + "WHERE\r\n"
						+ "    PM_HDR_ID = ? AND TENANT_ID = ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry, designHdrRequest.getPmHdrId(), designHdrRequest.getTenantId());
				int count = Integer.parseInt(resultMap.get("count").toString());

				if (count > 0) {
					String qrySelect = "SELECT CAST(START_INDENT_REQUEST AS UNSIGNED) as START_INDENT_REQUEST FROM design_hdr WHERE PM_HDR_ID=? AND TENANT_ID=?";
					String qryUpdate = "UPDATE design_hdr SET START_INDENT_REQUEST=? WHERE PM_HDR_ID=?";
					Map<String,Object> resultData = jdbcTemplate.queryForMap(qrySelect,
							designHdrRequest.getPmHdrId(), designHdrRequest.getTenantId());
					String startIndentReq = resultData.get("START_INDENT_REQUEST").toString();
					if ("1".equals(startIndentReq)) {
						res = this.jdbcTemplate.update(qryUpdate, "0", designHdrRequest.getPmHdrId());
						if (res > 0) {
							res = 0;
						} else {
							res = 2;
						}
						returnType=2;
						returnMsg=49;
						
					} else {
						res = this.jdbcTemplate.update(qryUpdate, "1", designHdrRequest.getPmHdrId());
						if (res > 0) {
							res = 1;
						} else {
							res = 2;
						}
						returnType=2;
						returnMsg=50;
					}
					commonNotifyMethod.InvokeNotificationMethod(returnType, returnMsg, null, designHdrRequest.getTenantId(), messageList, otherEmpId, "0", designHdrRequest.getPmId(), designHdrRequest.getMstId(), null);
				}
			}
		} catch (Exception ex) {
			logger.error("updateDesignHdr error " + ex.getMessage());
			res = 2;
		}
		return res;
	}

	@Override
	public String getProjectInitiationMstResp(ProjectInitiationMstRequest projectInitiation, String tenantId) {
		String res = "";
		try {
			String desig = uploadManagementDAO.getDesigCodeByEmpId(projectInitiation.getEmpId(), "");
			String qrySelect = "SELECT \r\n" + "    COUNT(*) as count\r\n" + "FROM\r\n" + "    project_wbs_initiation_mst\r\n"
					+ "WHERE\r\n" + "    FIND_IN_SET( ? , MASTER_POC)\r\n" + "        AND PM_ID = ? AND TENANT_ID= ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qrySelect, desig, projectInitiation.getPmId(),tenantId);
			int count = Integer.parseInt(resultMap.get("count").toString());
			if (count > 0) {
				res = "1";
			} else {
				res = "0";
			}
		} catch (Exception ex) {
			logger.error("getProjectInitiationMstResp error " + ex.getMessage());
			res = "2";
		}
		return res;
	}

	@Override
	public int updateAssyMstResp(AssyMstRequest assyMstRequest,List<String> otherEmpId, List<String> messageList) {
		int res = 0,returnType=0,returnMsg=0;

		try {
			if (assyMstRequest.getIsStatus().equalsIgnoreCase("0")) {
				// 0 means onLoad
				String Qry = "SELECT \r\n" + "    COUNT(*) as count\r\n" + "FROM\r\n" + "    assy_hdr\r\n" + "WHERE\r\n"
						+ "    PM_HDR_ID = ? AND TENANT_ID =  ? ";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry, assyMstRequest.getPmHdrId(),  assyMstRequest.getTenantId());
				int count = Integer.parseInt(resultMap.get("count").toString());
				if (count > 0) {
					String qrySelect = "SELECT  CAST(START_MATERIAL_REQUEST AS UNSIGNED) as START_MATERIAL_REQUEST FROM assy_hdr WHERE PM_HDR_ID=? AND TENANT_ID=?";
					Map<String,Object> resultData = jdbcTemplate.queryForMap(qrySelect, assyMstRequest.getPmHdrId(),
							assyMstRequest.getTenantId());
					res = Integer.parseInt(resultData.get("START_MATERIAL_REQUEST").toString());
					
				}

			} else {
				// 1 means onClick
				String Qry = "SELECT \r\n" + "    COUNT(*) as count\r\n" + "FROM\r\n" + "    assy_hdr\r\n" + "WHERE\r\n"
						+ "    PM_HDR_ID = ? AND TENANT_ID = ?";
				Map<String,Object> resultData = jdbcTemplate.queryForMap(Qry, assyMstRequest.getPmHdrId(),
						assyMstRequest.getTenantId());
				int count = Integer.parseInt(resultData.get("count").toString());
				if (count > 0) { 
					String qrySelect = "SELECT CAST(START_MATERIAL_REQUEST AS UNSIGNED) as START_MATERIAL_REQUEST FROM assy_hdr WHERE PM_HDR_ID=? AND TENANT_ID=?";
					String qryUpdate = "UPDATE assy_hdr SET START_MATERIAL_REQUEST=? WHERE PM_HDR_ID=?";
					Map<String,Object> resultMap = jdbcTemplate.queryForMap(qrySelect,
							assyMstRequest.getPmHdrId(), assyMstRequest.getTenantId());
					String startIndentReq = resultMap.get("START_MATERIAL_REQUEST").toString();    
					if ("1".equals(startIndentReq)) {
						res = this.jdbcTemplate.update(qryUpdate, "0", assyMstRequest.getPmHdrId());
						if (res > 0) {
							res = 0;
						} else {
							res = 2;
						}
						returnType=2;
						returnMsg=52;
					} else {
						res = this.jdbcTemplate.update(qryUpdate, "1", assyMstRequest.getPmHdrId());
						if (res > 0) {
							res = 1;
						} else {
							res = 2;
						}
						returnType=2;
						returnMsg=51;
					}
					commonNotifyMethod.InvokeNotificationMethod(returnType, returnMsg, null, assyMstRequest.getTenantId(), messageList, otherEmpId, "0", assyMstRequest.getPmId(), assyMstRequest.getMstId(), null);
				}
			}
		} catch (Exception ex) {
			logger.error("updateAssyMstResp error " + ex.getMessage());
			res = 2;
		}
		return res;
	}

	@Override
	public String getMinMaxDate(String funcName, String pmHdrId, String fieldName) {
		String resp = "";
		
		try {

			if (fieldName.equalsIgnoreCase("PLANNED_END_DATE") && funcName.equalsIgnoreCase("max")) {
	//		String getQ = 	"select max(PLANNED_END_DATE) AS DATE from project_timeline where PM_HDR_ID = ?";
				String getQ="Select DUE_DATE AS DATE from project_hdr where PM_HDR_ID = ? ";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ,pmHdrId);
				resp = resultMap.get("DATE").toString();
			}else if(fieldName.equalsIgnoreCase("PLANNED_START_DATE") && funcName.equalsIgnoreCase("min")) {
		//	String getQ = "select min(PLANNED_START_DATE) as DATE  from project_timeline where PM_HDR_ID = ?";
		String getQ= "Select case when ehdr.PROJECT_HANDOVER_DATE is null then hdr.CREATED_DATE else ehdr.PROJECT_HANDOVER_DATE end As DATE from project_hdr hdr inner join sales_enq_hdr ehdr on hdr.ENQUIRY_ID = ehdr.SE_ID where PM_HDR_ID = ? ";		
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ,pmHdrId);
			resp = resultMap.get("DATE").toString();
			}
		} catch (Exception ex) {
			logger.error("updatePlanStartDate error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public void updatePlanStartAndEndDate(String maxValue, String minValue, String pmHdrId) {
		try {

			String getQ = "UPDATE project_hdr SET PLANNED_START_DATE=?, PLANNED_END_DATE=? WHERE PM_HDR_ID=?";
			this.jdbcTemplate.update(getQ, minValue, maxValue, pmHdrId);

		} catch (Exception ex) {
			logger.error("updatePlanStartDate error " + ex.getMessage());
		}

	}

	@Override
	public int updateQCbuyoff(AssyMstRequest assyMstRequest) {
		int res = 0;

		try {
			if (assyMstRequest.getIsStatus().equalsIgnoreCase("0")) {
				// 0 means onLoad
				String Qry = "SELECT \r\n" + "    COUNT(*) as count\r\n" + "FROM\r\n" + "    quality_hdr\r\n" + "WHERE\r\n"
						+ "    PM_HDR_ID = ? AND TENANT_ID = ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry, assyMstRequest.getPmHdrId(),  assyMstRequest.getTenantId());
				int count = Integer.parseInt(resultMap.get("count").toString());
				if (count > 0) {
					String qrySelect = "SELECT INTERNAL_BUY_OFF_REQ FROM quality_hdr WHERE PM_HDR_ID=? AND TENANT_ID=?";
					Map<String,Object> resultData = jdbcTemplate.queryForMap(qrySelect, assyMstRequest.getPmHdrId(),  assyMstRequest.getTenantId());
					res = Integer.parseInt(resultData.get("INTERNAL_BUY_OFF_REQ").toString());
				}

			} else {
				// 1 means onClick
				String Qry = "SELECT \r\n" + "    COUNT(*) as count\r\n" + "FROM\r\n" + "    quality_hdr\r\n" + "WHERE\r\n"
						+ "    PM_HDR_ID = ? AND TENANT_ID = ?";
				Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry, assyMstRequest.getPmHdrId(),  assyMstRequest.getTenantId());
				int count = Integer.parseInt(resultMap.get("count").toString());
				if (count > 0) {

					String qryUpdate = "UPDATE quality_hdr SET INTERNAL_BUY_OFF_REQ=? WHERE PM_HDR_ID=?";
					res = this.jdbcTemplate.update(qryUpdate, "1", assyMstRequest.getPmHdrId());

				}
			}
		} catch (Exception ex) {
			logger.error("updateAssyMstResp error " + ex.getMessage());
			res = 2;
		}
		return res;
	}

	@Override
	public void UpdateQCStatus(String pmHdrId, String tenantId) {
		try {
			String stat = GetPropertyValue.getPropValue("QC_INSP_BUY_OFF", tenantId, jdbcTemplate);

			String getQ = "select case when count(*)>0 then Q_HDR_ID else 0 end as qc from quality_hdr where PM_HDR_ID= ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ, pmHdrId);
			int qHdr = Integer.parseInt(resultMap.get("qc").toString());
			if (qHdr > 0) {
				String updateQ = "update  quality_internal_buy set TRANSACTION_STATUS = ? where MASTER_ID = ?";
				this.jdbcTemplate.update(updateQ, stat, qHdr);
			}

		} catch (Exception ex) {
			logger.error("UpdateQCStatus error " + ex.getMessage());
		}

	}

	@Override
	public List<ProjectTimelineEntity> getTimeTrackerByProjectId(ProjectByIDRequest projHdr) {
		List<ProjectTimelineEntity> resp = new ArrayList<>();
		try {

			String getQ = "SELECT \r\n"
					+ "    ptime.MILESTONE_NAME,\r\n"
					+ "    ptime.PLANNED_START_DATE,\r\n"
					+ "    ptime.PLANNED_END_DATE,\r\n"
					+ "    em.EMPLOYEE_FIRSTNAME,\r\n"
					+ "     em.EMPLOYEE_ID,\r\n"
					+ "      dept.DEPARTMENT_CODE,\r\n"
					+ "    dept.DEPARTMENT_NAME\r\n"
					+ "FROM\r\n"
					+ "    project_timeline ptime,\r\n"
					+ "    employee_mst em,\r\n"
					+ "    department dept\r\n"
					+ "WHERE\r\n"
					+ "    em.EMPLOYEE_ID = ptime.RESPONSIBLE_NAME\r\n"
					+ "        AND dept.DEPARTMENT_CODE = ptime.RESPONSIBLE_DEPT_CODE\r\n"
					+ "        AND ptime.PM_HDR_ID = '"+projHdr.getProjectID()+"'\r\n"
					+ "        AND ptime.TENANT_ID = '"+projHdr.getTenantID()+"'";
			resp = this.jdbcTemplate.query(getQ, new ProjectTimelineEntityRowMapper());

		} catch (Exception ex) {
			logger.error("getTimeTrackerByProjectId error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public String getStartDateByProjId(String projectID, String tenantID,String departmentCode) {
		List<String> resp = new ArrayList<>();
		String startDte="";
		try {

			String getQ = "SELECT \r\n"
					+ "    tdl.PLANNED_START_DATE AS ACTUAL_STARTDATE\r\n"
					+ "FROM\r\n"
					+ "    task_entry_hdr thl,\r\n"
					+ "    task_entry_dtl tdl\r\n"
					+ "WHERE\r\n"
					+ "    thl.TE_HDR_ID = tdl.TE_HDR_ID\r\n"
					+ "        AND thl.DEPARTMENT_CODE = '"+departmentCode+"'\r\n"
					+ "        AND thl.PM_HDR_ID = '"+projectID+"'\r\n"
					+ "        AND thl.TENANT_ID = '"+tenantID+"'\r\n"
					+ "ORDER BY tdl.PLANNED_START_DATE ASC";
			resp = this.jdbcTemplate.queryForList(getQ,String.class);
			if(resp!=null && resp.size()>0) {
				startDte=resp.get(0);
			}

		} catch (Exception ex) {
			logger.error("getStartDateByProjId error " + ex.getMessage());
		}
		return startDte;
	}

	@Override
	public String getEndDateByProjId(String projectID, String tenantID,String departmentCode) {
		List<String> resp = new ArrayList<>();
		String endDte="";
		try {

			String nullCheck="SELECT \r\n"
					+ "    COUNT(*) as count \r\n"
					+ "FROM\r\n"
					+ "    task_entry_hdr thl,\r\n"
					+ "    task_entry_dtl tdl\r\n"
					+ "WHERE\r\n"
					+ "    thl.TE_HDR_ID = tdl.TE_HDR_ID\r\n"
					+ "        AND thl.DEPARTMENT_CODE = ? \r\n"
					+ "        AND thl.PM_HDR_ID = ? \r\n"
					+ "        AND thl.TENANT_ID = ? \r\n"
					+ "        AND tdl.COMPLETED_DATE IS NULL\r\n"
					+ "ORDER BY tdl.COMPLETED_DATE DESC";

			Map<String,Object> resultMap = jdbcTemplate.queryForMap(nullCheck, departmentCode, projectID, tenantID);
			int count = Integer.parseInt(resultMap.get("count").toString());

			if(count==0) {
				String getQ = "SELECT \r\n"
						+ "    tdl.COMPLETED_DATE AS ACTUAL__END_DATE\r\n"
						+ "FROM\r\n"
						+ "    task_entry_hdr thl,\r\n"
						+ "    task_entry_dtl tdl\r\n"
						+ "WHERE\r\n"
						+ "    thl.TE_HDR_ID = tdl.TE_HDR_ID\r\n"
						+ "        AND thl.DEPARTMENT_CODE = '"+departmentCode+"'\r\n"
						+ "        AND thl.PM_HDR_ID = '"+projectID+"'\r\n"
						+ "        AND thl.TENANT_ID = '"+tenantID+"'\r\n"
						+ "ORDER BY  tdl.COMPLETED_DATE DESC";
				resp = this.jdbcTemplate.queryForList(getQ,String.class);
				if(resp!=null && resp.size()>0) {
					endDte=resp.get(0);
				}
			}
		} catch (Exception ex) {
			logger.error("getEndDateByProjId error " + ex.getMessage());
		}
		return endDte;
	}
	@Override
	public List<GetTaskEntryDtlEntity> getdesignTaskDtlByProjectId(String ttCode, String tcCode,
			String projectID, String tenantid, String dependentId, String department) {
		List<GetTaskEntryDtlEntity> list = new ArrayList<GetTaskEntryDtlEntity>();
		try {
			String typeCode = "";
			String typeCategory = "";
			String hdrcheck = "";
			if (ttCode.equalsIgnoreCase("getAll")) {
				typeCode = "%%";
			} else {
				typeCode = "%" + ttCode + "%";
			}

			if (tcCode.equalsIgnoreCase("getAll")) {
				typeCategory = "%%";
			} else {
				typeCategory = "%" + tcCode + "%";
			}
			if (dependentId.equalsIgnoreCase("")) {
				hdrcheck = " is null and  hdr.TT_HDR_ID > 0";
			} else if (dependentId.equalsIgnoreCase("getAll")) {
				hdrcheck = "like '%%' and hdr.TT_HDR_ID = 0";
			} else {
				hdrcheck = "=" + dependentId + " and  hdr.TT_HDR_ID = 0";
			}

			String taskHdrStr = "SELECT \r\n" + "    dtl.*,\r\n" + "    dp.DEPARTMENT_NAME AS DEPARTMENT_NAME,\r\n"
					+ "    ttm.TT_DESC AS TT_DESC,dtl.REQUIREMENT_FROM,dtl.QTY,hdr.TASK_CATEGORY_CODE,hdr.TASK_TYPE_CODE,\r\n"
					+ "    tcm.TC_DESC AS TC_DESC, dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,em.EMPLOYEE_FIRSTNAME AS EMPLOYEE_FIRSTNAME\r\n"
					+ "FROM\r\n" + "    task_entry_hdr hdr\r\n" + "        INNER JOIN\r\n"
					+ "    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + "        LEFT JOIN\r\n"
					+ "    department dp ON hdr.DEPARTMENT_CODE = dp.DEPARTMENT_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_type_mst ttm ON ttm.TT_CODE = hdr.TASK_TYPE_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    task_category_mst tcm ON tcm.TC_CODE = hdr.TASK_CATEGORY_CODE\r\n" + "        LEFT JOIN\r\n"
					+ "    document_status_type_code dst ON dst.DOCUMENT_STATUS_TYPE_CODE = dtl.APPROVAL_STATUS\r\n"
					+ "        LEFT JOIN\r\n" + "    employee_mst em ON em.EMPLOYEE_ID = dtl.ASSIGNED_TO\r\n"
					+ "WHERE\r\n" + "    hdr.TASK_TYPE_CODE LIKE ? \r\n"
					+ "        AND hdr.TASK_CATEGORY_CODE LIKE ? \r\n"
					+ "        AND hdr.PM_HDR_ID = ? and hdr.DEPARTMENT_CODE = ? and hdr.TENANT_ID = ? and hdr.DEPENDENT_TE_HDR_ID "
					+ hdrcheck + "  order by IS_COMPLETED asc, DUE_DATE asc";

			list = this.jdbcTemplate.query(taskHdrStr, new GetTaskEntryDtlRowMapper(), typeCode, typeCategory, projectID,
					department, tenantid);

		} catch (Exception ex) {
			logger.error("getTaskTypeDtl Error" + ex);
		}
		return list;
	}

	@Override
	public int getIndentBudgetCheck(String pkaId, String sbExtnId) {
		int updateStatus = 0;
		try {
			String qry = "select count(*) as count from indent_budget_dtl where PKA_ID = ? and SB_EXTN_ID = ? and BUDGET_QTY >0 ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(qry, pkaId, sbExtnId);
			updateStatus = Integer.parseInt(resultMap.get("count").toString());
		} catch (Exception ex) {
			logger.error("getIndentBudgetCheck error " + ex.getMessage());
		}
		return updateStatus;
	}

	@Override
	public String getCostFlowTypeByPkaId(String pkaId) {
		String costFlowType = "LEGACY";
		try {
			String qry = "SELECT ph.COST_FLOW_TYPE FROM project_key_area pka "
					+ "INNER JOIN project_hdr ph ON pka.PM_HDR_ID = ph.PM_HDR_ID "
					+ "WHERE pka.PKA_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pkaId);
			if (resultMap.get("COST_FLOW_TYPE") != null) {
				costFlowType = resultMap.get("COST_FLOW_TYPE").toString();
			}
		} catch (Exception ex) {
			logger.error("getCostFlowTypeByPkaId error " + ex.getMessage());
		}
		return costFlowType;
	}

	@Override
	public String getIsInternalByPkaId(String pkaId) {
		String isInternal = "0";
		try {
			String qry = "SELECT se.IS_INTERNAL FROM project_key_area pka "
					+ "INNER JOIN project_hdr ph ON pka.PM_HDR_ID = ph.PM_HDR_ID "
					+ "INNER JOIN sales_enq_hdr se ON ph.ENQUIRY_ID = se.SE_ID "
					+ "WHERE pka.PKA_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pkaId);
			if (resultMap.get("IS_INTERNAL") != null) {
				isInternal = resultMap.get("IS_INTERNAL").toString();
			}
		} catch (Exception ex) {
			logger.error("getIsInternalByPkaId error " + ex.getMessage());
		}
		return isInternal;
	}

	@Override
	public Map<String, String> getCostFlowTypeGroupedByPmHdrIds(List<String> pmHdrIds) {
		Map<String, String> costFlowTypeByPmHdrId = new HashMap<>();
		if (pmHdrIds == null || pmHdrIds.isEmpty()) {
			return costFlowTypeByPmHdrId;
		}
		try {
			String placeholders = String.join(",", pmHdrIds.stream().map(id -> "?").toArray(String[]::new));
			String qry = "SELECT PM_HDR_ID, COST_FLOW_TYPE FROM project_hdr WHERE PM_HDR_ID IN (" + placeholders + ")";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(qry, pmHdrIds.toArray());
			for (Map<String, Object> row : rows) {
				String costFlowType = row.get("COST_FLOW_TYPE") != null ? row.get("COST_FLOW_TYPE").toString() : "LEGACY";
				costFlowTypeByPmHdrId.put(row.get("PM_HDR_ID").toString(), costFlowType);
			}
		} catch (Exception ex) {
			logger.error("getCostFlowTypeGroupedByPmHdrIds error " + ex.getMessage());
		}
		return costFlowTypeByPmHdrId;
	}

	@Override
	public Map<String, String> getIsInternalGroupedByPmHdrIds(List<String> pmHdrIds) {
		Map<String, String> isInternalByPmHdrId = new HashMap<>();
		if (pmHdrIds == null || pmHdrIds.isEmpty()) {
			return isInternalByPmHdrId;
		}
		try {
			String placeholders = String.join(",", pmHdrIds.stream().map(id -> "?").toArray(String[]::new));
			String qry = "SELECT ph.PM_HDR_ID AS PM_HDR_ID, se.IS_INTERNAL AS IS_INTERNAL "
					+ "FROM project_hdr ph INNER JOIN sales_enq_hdr se ON ph.ENQUIRY_ID = se.SE_ID "
					+ "WHERE ph.PM_HDR_ID IN (" + placeholders + ")";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(qry, pmHdrIds.toArray());
			for (Map<String, Object> row : rows) {
				String isInternal = row.get("IS_INTERNAL") != null ? row.get("IS_INTERNAL").toString() : "0";
				isInternalByPmHdrId.put(row.get("PM_HDR_ID").toString(), isInternal);
			}
		} catch (Exception ex) {
			logger.error("getIsInternalGroupedByPmHdrIds error " + ex.getMessage());
		}
		return isInternalByPmHdrId;
	}

	@Override
	public String getCostFlowTypeByPmHdrId(String pmHdrId) {
		String costFlowType = "LEGACY";
		try {
			String qry = "SELECT COST_FLOW_TYPE FROM project_hdr WHERE PM_HDR_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId);
			if (resultMap.get("COST_FLOW_TYPE") != null) {
				costFlowType = resultMap.get("COST_FLOW_TYPE").toString();
			}
		} catch (Exception ex) {
			logger.error("getCostFlowTypeByPmHdrId error " + ex.getMessage());
		}
		return costFlowType;
	}

	@Override
	public String getpmHdrIdByPkaId(String pkaId) {
		String resp = "";
		try {

			String getQ = "SELECT \n"
					+ "    pka.PM_HDR_ID as PM_HDR_ID \n"
					+ "FROM\n"
					+ "    project_key_area_extn extn\n"
					+ "        INNER JOIN\n"
					+ "    project_key_area pka ON extn.PKA_ID = pka.PKA_ID\n"
					+ "WHERE\n"
					+ "    pka.PKA_ID = ? limit 1 ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ, pkaId);
			resp = resultMap.get("PM_HDR_ID").toString();

		} catch (Exception ex) {
			logger.error("getpmHdrIdByPkaId error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public String getprojKeyMstDesc(String pkaId) {
		String resp = "";
		try {

			String getQ = "SELECT \n"
					+ "   case when count(*) > 0 then PK_DESC else '' end AS  PK_DESC \n"
					+ "FROM\n"
					+ "    project_key_area_mst\n"
					+ "WHERE\n"
					+ "    PK_ID = ? ";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(getQ, pkaId);
			resp = resultMap.get("PK_DESC").toString();

		} catch (Exception ex) {
			logger.error("getprojKeyMstDesc error " + ex.getMessage());
		}
		return resp;
	}

	@Override
	public List<String> getAssignedMembersForProject(String pmHdrId, String tenantId) {
		List<String> empList = new ArrayList<>();
		try {
			String Qry = "select distinct(ASSIGNED_EMP_ID) from process_assigned_team where PROJECT_ID='"+pmHdrId+"' and TENANT_ID='"+tenantId+"';";
			empList = this.jdbcTemplate.queryForList(Qry,String.class);
		} catch (Exception ex) {
			logger.error("getAssignedMembersForProject Error" + ex);
		}
		return empList;
	}

	@Override
	public String projectDueDate(String pmId, String tenantId) {
		String projectDueDate = "";
		try {

			String projectDueDateString = "SELECT DUE_DATE FROM project_hdr where PM_HDR_ID  = ? and TENANT_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(projectDueDateString, pmId, tenantId);
			projectDueDate = resultMap.get("DUE_DATE").toString();

		} catch (Exception ex) {
			logger.error("projectDueDate error " + ex.getMessage());
		}
		return projectDueDate;
	}

	@Override
	public String getCompletionPercent(String pmHdrId, String tenantID) {
		// TODO Auto-generated method stub
        BigDecimal percentage = BigDecimal.ZERO;               ;
		try {
			
			String Qry="SELECT \r\n" + 
					"    CASE\r\n" + 
					"        WHEN COUNT(*) > 0 THEN ROUND(AVG(COMPLETED_PTG), 2)\r\n" + 
					"        ELSE 0.00\r\n" + 
					"    END AVG_PERCENTAGE\r\n" + 
					"FROM\r\n" + 
					"    task_entry_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    task_entry_dtl dtl ON hdr.TE_HDR_ID = dtl.TE_HDR_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.TENANT_ID = ?\r\n" + 
					"        AND hdr.DEPENDENT_TE_HDR_ID IS NULL\r\n" + 
					"        AND hdr.PM_HDR_ID = ?\r\n" + 
					"        AND hdr.TT_HDR_ID > 0;";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry, tenantID, pmHdrId);

			percentage=(BigDecimal) resultMap.get("AVG_PERCENTAGE");
			
			
		}catch(Exception ex) {
			logger.error("getCompletionPercent error " + ex.getMessage());
		}
		return percentage.toString();
	}

	@Override
	public String getTargetCost(String pmHdrId, String tenantID) {
		// TODO Auto-generated method stub
		
        BigDecimal tragetCost = BigDecimal.ZERO;               ;

		try {
			String Qry="select case when count(*) then sum(TARGET_VALUE) "
					+ "else 0.00 end Target_Value from indent_hdr"
					+ " where PROJECT_ID=? and TENANT_ID=?;\r\n" ;
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry, pmHdrId,tenantID );

			tragetCost=(BigDecimal) resultMap.get("Target_Value");
		}catch(Exception ex) {
			logger.error("getTargetCost error " + ex.getMessage());
		}
		return tragetCost.toString();
	}

	@Override
	public List<BudgetSheetPaymentEntity> getBudgetSheetPaymentTerms(String sbHdrId) {
		List<BudgetSheetPaymentEntity> returnList = new ArrayList<BudgetSheetPaymentEntity>();
		try {
			String retQry = "SELECT * FROM budget_sheet_payment_terms WHERE BS_HDR_ID = ?";

			returnList = this.jdbcTemplate.query(retQry, new BudgetSheetPaymentRowMapper(), sbHdrId);

		} catch (Exception ex) {
			logger.error("getBudgetSheetPaymentTerms error---> " + ex);
		}
		return returnList;
	}



	@Override
	public int updateBudgetSheetPaymentTerms(String sbPtId, String actualDate, String remarks) {
		int update=0;
		try {
			String getQ = "UPDATE budget_sheet_payment_terms SET ACTUAL_DATE=?, REMARKS=? WHERE BS_POT_ID=?";
			update = this.jdbcTemplate.update(getQ, actualDate, remarks, sbPtId);

		} catch (Exception ex) {
			logger.error("updateBudgetSheetPaymentTerms error " + ex.getMessage());
		}
		return update;
	}

	@Override
	public String getDebitVal(String pmHdrId, String tenantID) {
        BigDecimal DebitNote = BigDecimal.ZERO;
		try {
			String Qry="select case when count(*) then sum(DN_VALUE) "
					+ "else 0.00 end DN_VALUE from debit_note"
					+ " where PM_HDR_ID=? and TENANT_ID=?;\r\n" ;
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(Qry, pmHdrId,tenantID );

			DebitNote=(BigDecimal) resultMap.get("DN_VALUE");
		}catch(Exception ex) {
			logger.error("getDebitVal error " + ex.getMessage());
		}
		return DebitNote.toString();
	}

	@Override
	public String getEmployeeCostByPmHdrId(String pmHdrId, String tenantId) {
		String employeeCost = "0";
		try {
			String qry = "SELECT COALESCE(SUM(td.TIMESHEET_COST),0) AS VAL " +
					"FROM timesheet_hdr th " +
					"INNER JOIN timesheet_dtl td ON td.T_HDR_ID = th.T_HDR_ID " +
					"WHERE th.PM_HDR_ID = ? AND th.TENANT_ID = ?";
			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry, pmHdrId, tenantId);
			employeeCost = resultMap.get("VAL").toString();
		} catch (Exception ex) {
			logger.error("getEmployeeCostByPmHdrId error " + ex.getMessage());
		}
		return employeeCost;
	}

}
