package com.vmfg.general.dao.impl;

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

import com.vmfg.assembly.RowMapper.GetAssyDtlRowMapper;
import com.vmfg.assembly.entity.GetAssyDtlEntity;
import com.vmfg.design.response.DesignHdr;
import com.vmfg.design.rowmapper.DesignHdrRowMapper;
import com.vmfg.finance.entity.FinanceHdrEntity;
import com.vmfg.finance.rowmapper.FinanceHdrRowMapper;
import com.vmfg.general.dao.interfaces.ITileViewDAO;
import com.vmfg.project.entity.ProjectHdr;
import com.vmfg.project.rowmapper.ProjectHdrRowMapper;
import com.vmfg.quality.RowMapper.GetQtyDtlRowMapper;
import com.vmfg.quality.entity.GetQtyDtlEntity;
import com.vmfg.sales.entity.SalesEnqDtlEntity;
import com.vmfg.sales.rowmapper.SalesEnqDtlRowMapper;
import com.vmfg.scm.entity.ProjectHdrDtlEntity;
import com.vmfg.scm.entity.ScmHdrBasedDtlEntity;
import com.vmfg.scm.entity.ScmHdrEntity;
import com.vmfg.scm.request.ScmHdrBasedDtlRequest;
import com.vmfg.scm.rowmapper.ScmHdrRowMapper;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class TileViewDAO implements ITileViewDAO{
	private static final Logger logger = LoggerFactory.getLogger(TileViewDAO.class);

	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<String> getDistinctStatus(String tableName) {
		List<String> currentStage = null;
		String currentStageStr = "";
		try {
			if(tableName.equalsIgnoreCase("sales_enq_hdr")) {
				currentStageStr = "select distinct(TRANSACTION_STATUS) as TRANSACTION_STATUS  from sales_enq_hdr where TRANSACTION_STATUS is not null order by TRANSACTION_STAGE_SEQ +0  , TRANSACTION_STATUS_SEQ +0 ";
			} else if(tableName.equalsIgnoreCase("design_hdr")) {
				currentStageStr = "select distinct(TRANSACTION_STATUS) as TRANSACTION_STATUS  from design_hdr where TRANSACTION_STATUS is not null order by TRANSACTION_STAGE_SEQ +0  , TRANSACTION_STATUS_SEQ +0 ";

			} else if(tableName.equalsIgnoreCase("project_hdr")) {
				currentStageStr = "select distinct(TRANSACTION_STATUS) as TRANSACTION_STATUS  from project_hdr where TRANSACTION_STATUS is not null order by TRANSACTION_STAGE_SEQ +0  , TRANSACTION_STATUS_SEQ +0 ";

			} else if(tableName.equalsIgnoreCase("scm_hdr")) {
				currentStageStr = "select distinct(TRANSACTION_STATUS) as TRANSACTION_STATUS  from scm_hdr where TRANSACTION_STATUS is not null order by TRANSACTION_STAGE_SEQ +0  , TRANSACTION_STATUS_SEQ +0 ";

			} else if(tableName.equalsIgnoreCase("finance_hdr")) {
				currentStageStr = "SELECT DISTINCT\r\n" + 
						"    (ph.TRANSACTION_STATUS) AS TRANSACTION_STATUS\r\n" + 
						"FROM\r\n" + 
						"    finance_hdr fh\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_hdr ph ON fh.PM_HDR_ID = ph.PM_HDR_ID\r\n" + 
						"WHERE\r\n" + 
						"    ph.TRANSACTION_STATUS IS NOT NULL\r\n" + 
						"ORDER BY ph.TRANSACTION_STAGE_SEQ + 0 , ph.TRANSACTION_STATUS_SEQ + 0;";

			} else if(tableName.equalsIgnoreCase("assy_hdr")) {
				currentStageStr = "select distinct(TRANSACTION_STATUS) as TRANSACTION_STATUS  from assy_hdr where TRANSACTION_STATUS is not null order by TRANSACTION_STAGE_SEQ +0  , TRANSACTION_STATUS_SEQ +0 ";

			} else if(tableName.equalsIgnoreCase("quality_hdr")) {
				currentStageStr = "select distinct(TRANSACTION_STATUS) as TRANSACTION_STATUS  from quality_hdr where TRANSACTION_STATUS is not null order by TRANSACTION_STAGE_SEQ +0  , TRANSACTION_STATUS_SEQ +0 ";

			}
			
			currentStage = jdbcTemplate.queryForList(currentStageStr,String.class);
			//currentStage = resultMap.get("TRANSACTION_STATUS").toString();

			
		} catch (Exception ex) {
			logger.error("getcurrentEnquiryStage Error" + ex);
		}
		return currentStage;
	}

	@Override
	public List<SalesEnqDtlEntity> saleEnqList(String status, String fromDate, String toDate, String customerName,
			String tenantId, String empId, String tentativeVal, String isexpectedDate) {
		
			List<SalesEnqDtlEntity> saleEnqDtl = new ArrayList<SalesEnqDtlEntity>();
			try {
				String custname="",dateDiff="";
				if(customerName.equalsIgnoreCase("")) {
					custname = "%%";
				}else {
					custname ="%"+customerName+"%";
				}
				String expectedCheck = "";
				if(isexpectedDate.equalsIgnoreCase("1")){
					expectedCheck = ",hdr.EXPECTED_PO_DATE ";
				}
				if(!fromDate.equalsIgnoreCase("")) {
					dateDiff = " hdr.ENQUIRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ";
				}
					
				String saleEnqDtlStr="SELECT \r\n" + 
						"    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS HDR_STATUS_DESC,\r\n" + 
						"    dstc1.DOCUMENT_STATUS_TYPE_DESCRIPTION AS SLAVE_STATUS_DESC,\r\n" + 
						"    sm.STG_DESC,\r\n" + 
						"    dtl.TRANSACTION_STATUS_SEQ AS SLAVE_STATUS,\r\n" + 
						"    hdr.*, case when sbsh.TOTAL_BUDGET_COST is null or 0 then hdr.TENTATIVE_PO_VALUE else (sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end FINAL_COST\r\n" + 
						"FROM\r\n" + 
						"    sales_enq_hdr hdr\r\n" + 
						"        INNER JOIN\r\n" + 
						"    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    stg_master sm ON sm.STG_CODE = hdr.TRANSACTION_STAGE\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    document_status_type_code dstc1 ON dstc1.DOCUMENT_STATUS_TYPE_CODE = dtl.TRANSACTION_STATUS\r\n" + 
						"        LEFT JOIN\r\n" + 
						"    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID \r\n" + 
						"WHERE\r\n" +dateDiff+ 
						"        CUSTOMER_NAME LIKE ?\r\n" + 
						"        AND hdr.TENANT_ID = ? AND pat.TENANT_ID = hdr.TENANT_ID \r\n" + 
						"        AND pat.ASSIGNED_EMP_ID = ?\r\n" + 
						"        AND pat.IS_ACTIVE = 1 and TENTATIVE_PO_VALUE >? \r\n" + 
						"        AND pat.PM_ID = 1 AND hdr.TRANSACTION_STATUS = '"+status+"' order by hdr.SE_ID"+expectedCheck+"";
				saleEnqDtl = this.jdbcTemplate.query(saleEnqDtlStr, new SalesEnqDtlRowMapper(),custname,tenantId,empId,tentativeVal);
			
		}catch (Exception ex) {
			logger.error("saleEnqList Error" + ex);
		}
		return 	saleEnqDtl;
	}

	@Override
	public List<DesignHdr> designTitleViewList(String status, String fromDate, String toDate, String customer,
			String tenantId,String projectId,String processId,String empId) {
		logger.debug("getDesignHdr DAO method start");
		List<DesignHdr> hdr = null;
		try {
			String custname = "";
			if (customer.equalsIgnoreCase("")) {
				custname = "%%";
			} else {
				custname ="%"+customer+"%";
			}

			String datediff="";
			if(!fromDate.equalsIgnoreCase("")) {
				datediff = "  hdr.REQUEST_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' AND";
			}
			String pro = " and hdr.PM_HDR_ID like '%%' ";
			if(!projectId.equalsIgnoreCase("getall")) {
				pro = " and hdr.PM_HDR_ID = '"+projectId+"'";
			}
			
				String getQ = "SELECT \r\n" + "    hdr.*,\r\n" + "    ds.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n"
						+ "    proj.ENQUIRY_ID,\r\n" + "    proj.PROJECT_CODE,\r\n" + "    em.EMPLOYEE_FIRSTNAME,\r\n"
						+ "    stg.STG_DESC\r\n" + "FROM\r\n" + "    design_hdr hdr,\r\n"
						+ "    document_status_type_code ds,\r\n" + "    employee_mst em,\r\n"
						+ "    project_hdr proj,\r\n" + "    process_assigned_team pat, stg_master stg\r\n"
						+ "WHERE\r\n" + datediff  + "     hdr.TENANT_ID = ?\r\n"
						+ "        and stg.STG_CODE = hdr.TRANSACTION_STAGE\r\n"
						+ "        AND hdr.TRANSACTION_STATUS = ds.DOCUMENT_STATUS_TYPE_CODE AND pat.TENANT_ID = hdr.TENANT_ID \r\n"
						+ "        AND proj.PM_HDR_ID = hdr.PM_HDR_ID\r\n"
						+ "        AND hdr.DE_HDR_ID = pat.MASTER_ID\r\n" + "        AND pat.PM_ID = ?\r\n"
						+ "        AND ASSIGNED_EMP_ID = ?\r\n" + "        AND hdr.CUSTOMER_NAME LIKE ?\r\n"
						+ "        AND hdr.REQUESTED_BY = em.EMPLOYEE_ID\r\n" + "  AND hdr.TRANSACTION_STATUS = '"+status+"'      AND pat.IS_ACTIVE = 1"+pro;

				RowMapper<DesignHdr> rowmapper = new DesignHdrRowMapper();
				hdr = this.jdbcTemplate.query(getQ, rowmapper, tenantId, processId, empId, custname);
		
//				int checkCount = GetPropertyValue.getPropValueCountCheck("DESIGN_LANDING_PAGE_INDENT_STATUS_CODE", tenantId, jdbcTemplate);
//				int verifiedCount = GetPropertyValue.getPropValueCountCheck("DESIGN_LANDING_PAGE_INDENT_STATUS_CODE_VERIFIED", tenantId, jdbcTemplate);
//				int approvedCount = GetPropertyValue.getPropValueCountCheck("DESIGN_LANDING_PAGE_INDENT_STATUS_CODE_APPROVED", tenantId, jdbcTemplate);
				
//				List<DocumentStatusTypeCode> dockTypeList = new ArrayList<DocumentStatusTypeCode>();
//				String documentStatusTypeCodeQry = "SELECT DISTINCT\r\n" + 
//						"    d.DOCUMENT_STATUS_TYPE_CODE,d.DOCUMENT_STATUS_TYPE_DESCRIPTION\r\n" + 
//						"FROM\r\n" + 
//						"    document_status_type_code d\r\n" + 
//						"        JOIN\r\n" + 
//						"    document_lifecycle_mst m ON d.DOCUMENT_STATUS_TYPE_CODE = m.DOC_STATUS\r\n" + 
//						"WHERE\r\n" + 
//						"    m.DOC_TYPE = 'DC018'\r\n" + 
//						"        AND m.DOC_GROUP = 'IT001'\r\n" + 
//						"        AND m.TENANT_ID = ? \r\n" + 
//						"        AND m.CURR_SEQUENCE != 1\r\n" + 
//						"ORDER BY m.CURR_SEQUENCE + 1";
//				
//				dockTypeList = this.jdbcTemplate.query(documentStatusTypeCodeQry, new DocumentStatusTypeCodeRowMapper(),tenantId);
				
				for(int q =0;q<hdr.size();q++) {
					hdr.get(q).setChecked("0");
					hdr.get(q).setVerified("0");
					hdr.get(q).setApproved("0");
				
					String	checkedCode = GetPropertyValue.getPropValue("DESIGN_LANDING_PAGE_INDENT_STATUS_CODE", tenantId, jdbcTemplate);
					String	verifiedCode = GetPropertyValue.getPropValue("DESIGN_LANDING_PAGE_INDENT_STATUS_CODE_VERIFIED", tenantId, jdbcTemplate);
					String	approvedCode = GetPropertyValue.getPropValue("DESIGN_LANDING_PAGE_INDENT_STATUS_CODE_APPROVED", tenantId, jdbcTemplate);
					
					
					String	designVerifiedCode = GetPropertyValue.getPropValue("DESIGN_LANDING_PAGE_DESIGN_STATUS_CODE_VERIFIED", tenantId, jdbcTemplate);
					String	designApprovedCode = GetPropertyValue.getPropValue("DESIGN_LANDING_PAGE_DESIGN_STATUS_CODE_APPROVED", tenantId, jdbcTemplate);

					for(int i =0;i<3;i++) {
						String code="";
						if(i==0) 
							code=checkedCode;
						if(i==1)
							code=verifiedCode;
						if(i==2)
							code=approvedCode;
						
						String indentCountStr = "select count(*) AS VALUE from indent_hdr where FIND_IN_SET(SEQUENCE_STATUS , ? ) and PROJECT_ID = ? and TENANT_ID = ?";
						Map<String, Object> result = jdbcTemplate.queryForMap(indentCountStr,code,hdr.get(q).getProjectID(),tenantId);
						int indentCount = Integer.parseInt(result.get("VALUE").toString());
						if(i==0)
							hdr.get(q).setChecked(Integer.toString(indentCount));
						if(i==1)
							hdr.get(q).setVerified(Integer.toString(indentCount));
						if(i==2) {
							hdr.get(q).setApproved(Integer.toString(indentCount));
						}
					}
					
//					String indentCountStr = "select count(*) as VALUE from indent_grp_scs scs inner join indent_hdr hdr on hdr.INDENT_ID=scs.INDENT_ID where FIND_IN_SET (scs.SEQUENCE_STATUS ,?) and hdr.PROJECT_ID = ? and hdr.TENANT_ID = ?";
					String indentCountStr = "select count(*) as VALUE from indent_grp_scs scs inner join indent_hdr hdr on hdr.INDENT_ID=scs.INDENT_ID where scs.SEQUENCE_NO = ? and hdr.PROJECT_ID = ? and hdr.TENANT_ID = ?";
					Map<String, Object> result = jdbcTemplate.queryForMap(indentCountStr, designVerifiedCode, hdr.get(q).getProjectID(),tenantId);
					String indentCount = result.get("VALUE").toString();
					hdr.get(q).setDesignVerified(indentCount);
					
//					String indentCount1Str = "select count(*) as VALUE from indent_grp_scs scs inner join indent_hdr hdr on hdr.INDENT_ID=scs.INDENT_ID where FIND_IN_SET (scs.SEQUENCE_STATUS ,?) and hdr.PROJECT_ID = ? and hdr.TENANT_ID = ?";
					String indentCount1Str = "select count(*) as VALUE from indent_grp_scs scs inner join indent_hdr hdr on hdr.INDENT_ID=scs.INDENT_ID where scs.SEQUENCE_NO = ? and hdr.PROJECT_ID = ? and hdr.TENANT_ID = ?";
					Map<String, Object> results = jdbcTemplate.queryForMap(indentCount1Str,designApprovedCode,hdr.get(q).getProjectID(),tenantId);
					String indentCount1 = results.get("VALUE").toString();
					hdr.get(q).setDesignApproved(indentCount1);
					
					String isInt = "select IS_INTERNAL from project_hdr hdr inner join sales_enq_hdr enq \r\n" + 
							"		 on hdr.ENQUIRY_ID = enq.SE_ID where PM_HDR_ID=?";
					Map<String, Object> resultMap = jdbcTemplate.queryForMap(isInt,hdr.get(q).getProjectID());
					String isInternal = resultMap.get("IS_INTERNAL").toString();
					hdr.get(q).setIsInternal(isInternal);
					
				}
		} catch (Exception ex) {
			logger.error("Error with method designTitleViewList " + ex.getMessage());
		}

		logger.debug("designTitleViewList DAO method end");
		return hdr;
	}

	@Override
	public List<ProjectHdr> projectTitleViewList(String tenantId, String custName, String fromDate, String toDate,
			String projectID, String empId, String pmId,String status) {

		List<ProjectHdr> projHdr = null;
		String dateDiff = "";
		if(!fromDate.equalsIgnoreCase("")) {
			dateDiff = " AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		String pro = " and hdr.PM_HDR_ID like '%%' ";
		if(!projectID.equalsIgnoreCase("")) {
			pro = " and hdr.PM_HDR_ID = '"+projectID+"'";
		}
		try {
				String getQ = "SELECT \r\n" + "    eqh.ENQUIRY_CODE,\r\n" + "    PM_HDR_ID, eqh.IS_INTERNAL,\r\n"
						+ "    hdr.PROJECT_CODE,\r\n" + "    hdr.PROJECT_NAME,\r\n" + "    hdr.PROJECT_DESCRIPTION,\r\n"
						+ "    ENQUIRY_ID,\r\n" + "    hdr.PRODUCT_DETAILS,\r\n" + "    hdr.CREATED_DATE,\r\n"
						+ "    hdr.DUE_DATE,\r\n" + "    hdr.CUSTOMER_NAME,\r\n" + "    pms.STG_CODE, shdr.SB_HDR_ID,\r\n"
						+ "    dst.DOCUMENT_STATUS_TYPE_DESCRIPTION,eqh.PROJECT_HANDOVER_DATE AS PROJECT_HANDOVER_DATE,\r\n"
						+ "    stg.STG_DESC,hdr.INITIATION_DATE,hdr.PLANNED_START_DATE,hdr.PLANNED_END_DATE,hdr.INDUSTRIAL_TYPE,hdr.SCOPE_OF_WORK,hdr.PRIORITY,\r\n"
						+ "    (shdr.TOTAL_BUDGET_COST + shdr.CR_COST) AS FINAL_SALE_VALUE \r\n"
						+ "FROM\r\n" + "    project_hdr hdr,\r\n" + "    process_config pms,\r\n"
						+ "    document_status_type_code dst,\r\n" + "    sales_enq_hdr eqh,\r\n"
						+ "    process_assigned_team pat,\r\n" + "sales_budget_sheet_hdr shdr, stg_master stg\r\n " + " WHERE\r\n"
						+ "    hdr.TRANSACTION_STATUS = dst.DOCUMENT_STATUS_TYPE_CODE\r\n"
						+ "        AND hdr.TRANSACTION_STAGE = pms.STG_CODE\r\n"
						+ "        AND shdr.MASTER_ID = hdr.ENQUIRY_ID\r\n"
						+ "        and stg.STG_CODE = pms.STG_CODE AND pat.TENANT_ID = hdr.TENANT_ID AND pms.TENANT_ID = eqh.TENANT_ID \r\n"
						+ "        AND pat.MASTER_ID = hdr.PM_HDR_ID\r\n" + "        AND pat.ASSIGNED_EMP_ID = ?\r\n"
						+ "        AND eqh.SE_ID = hdr.ENQUIRY_ID\r\n" + "        AND hdr.CUSTOMER_NAME LIKE ?\r\n"
						+ "        \r\n" + dateDiff +"        AND hdr.TENANT_ID = ?\r\n"
						+ "        AND pat.PM_ID = ?\r\n" + "        AND pat.IS_ACTIVE = 1 AND hdr.TRANSACTION_STATUS = '"+status+"'" + pro;
				projHdr = this.jdbcTemplate.query(getQ, new ProjectHdrRowMapper(), empId, "%" + custName + "%",
						 tenantId, pmId);
			

		} catch (Exception ex) {
			logger.error("getProjectDtl error " + ex.getMessage());
		}
		return projHdr;
	}

	@Override
	public List<ScmHdrBasedDtlEntity> scmTitleViewList(ScmHdrBasedDtlRequest scmHdrBasedDtl,String status) {
		List<ScmHdrEntity> list = new ArrayList<>();
		List<ScmHdrBasedDtlEntity> scmHdrBasedDtlList = new ArrayList<>();
		String getQ = "",datediff="";
		if(!scmHdrBasedDtl.getFromDate().equalsIgnoreCase("")) {
			datediff =  " sh.SCM_INITIATED_DATE BETWEEN '"+scmHdrBasedDtl.getFromDate()+"' and '"+scmHdrBasedDtl.getToDate()+"' and ";
		}
		
		
		String pro = " and phdr.PM_HDR_ID like '%%' ";
		if(!scmHdrBasedDtl.getProjectId().equalsIgnoreCase("")) {
			pro = " and phdr.PM_HDR_ID = '"+scmHdrBasedDtl.getProjectId()+"'";
		}
		try {
			getQ = "SELECT \r\n" + "    @a:=@a + 1 serial_number,\r\n" + "    sh.DUE_DATE,  eqh.IS_INTERNAL,\r\n"
						+ "    sh.SCM_HDR_ID,\r\n" + "    sh.SCM_INITIATED_DATE,\r\n" + "    sh.PM_HDR_ID,\r\n"
						+ "    sh.TRANSACTION_STATUS,\r\n" + "    sh.TRANSACTION_STATUS_SEQ,\r\n"
						+ "    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + "    phdr.ENQUIRY_ID\r\n" + "FROM\r\n"
						+ "    (SELECT @a:=0) AS a,\r\n" + "    scm_hdr AS sh\r\n" + "        INNER JOIN\r\n"
						+ "    document_status_type_code AS dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = sh.TRANSACTION_STATUS\r\n"
						+ "        INNER JOIN\r\n" + "    process_assigned_team pa ON pa.MASTER_ID = sh.SCM_HDR_ID\r\n"
						+ "        INNER JOIN\r\n" + "    project_hdr phdr ON phdr.PM_HDR_ID = sh.PM_HDR_ID\r\n"
						+ "        INNER JOIN  sales_enq_hdr eqh ON eqh.SE_ID = phdr.ENQUIRY_ID \r\n"
						+ "WHERE\r\n" +datediff +"  sh.TENANT_ID = '"
						+ scmHdrBasedDtl.getTenantId() + "' \r\n" + "        AND pa.ASSIGNED_EMP_ID = '"
						+ scmHdrBasedDtl.getEmpId() + "' \r\n" + "        AND pa.PM_ID = 5 AND pa.TENANT_ID = sh.TENANT_ID  \r\n"
						+ "        AND pa.IS_ACTIVE = 1 and sh.TRANSACTION_STATUS = '"+status+"' and phdr.CUSTOMER_NAME LIKE '%"+scmHdrBasedDtl.getCustomerName()+"%' " + pro ;

			
			list = this.jdbcTemplate.query(getQ, new ScmHdrRowMapper());

			for (ScmHdrEntity obj : list) {
				ScmHdrBasedDtlEntity scmHdrObj = new ScmHdrBasedDtlEntity();
				
				String projCountQry = "SELECT \r\n" + 
						"    COUNT(dtl.INDENT_DTL_ID) AS INDENT_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    indent_dtl dtl\r\n" + 
						"        INNER JOIN\r\n" + 
						"    indent_hdr AS ih ON dtl.INDENT_ID = ih.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    scm_hdr AS sh ON sh.PM_HDR_ID = ih.PROJECT_ID\r\n" + 
						"WHERE\r\n" + 
						"    ih.PROJECT_ID = ? \r\n" + 
						"        AND ih.SEQUENCE_STATUS IN ('DS020' , 'DS019', 'DS070', 'DS077')";
				
				Map<String, Object> resultMap = jdbcTemplate.queryForMap(projCountQry,obj.getPmHdrId());
				int intentCount = Integer.parseInt(resultMap.get("INDENT_DTL_ID").toString());
				
				String intentCountQry = "SELECT \r\n" + 
						"    count(pod.PO_DTL_ID) as PO_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    indent_hdr AS ih\r\n" +  
						"        INNER JOIN\r\n" + 
						"    po_hdr AS poh ON poh.INDENT_ID = ih.INDENT_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    po_dtl pod ON poh.PO_ID = pod.PO_ID\r\n" + 
						"WHERE\r\n" + 
						"    ih.PROJECT_ID = ?  AND poh.IS_APPROVED=1 AND IS_LATEST=1 and poh.SEQUENCE_STATUS !='DS100'";

				Map<String, Object> result = jdbcTemplate.queryForMap(intentCountQry,obj.getPmHdrId());
				int poCount = Integer.parseInt(result.get("PO_DTL_ID").toString());

				String inwardCountQry = "  SELECT \r\n" + 
						"    COUNT(mid.MI_DTL_ID) AS MI_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    scm_hdr hdr,\r\n" + 
						"    indent_hdr ihdr,\r\n" + 
						"    po_hdr po,\r\n" + 
						"    material_inward_hdr mih,\r\n" + 
						"    material_inward_dtl mid\r\n" + 
						"WHERE\r\n" + 
						"    hdr.PM_HDR_ID = ihdr.PROJECT_ID\r\n" + 
						"        AND ihdr.INDENT_ID = po.INDENT_ID\r\n" + 
						"        AND po.PO_ID = mih.PO_ID\r\n" + 
						"        And mih.MI_ID=mid.MI_ID\r\n" + 
						"        AND hdr.PM_HDR_ID = ? AND mih.IS_COMPLETED=1";

				Map<String, Object> results = jdbcTemplate.queryForMap(inwardCountQry,obj.getPmHdrId());
				int inwardCount = Integer.parseInt(results.get("MI_DTL_ID").toString());

				String grnCountQry = "SELECT \r\n" + 
						"    COUNT(gdtl.GRN_DTL_ID) AS GRN_DTL_ID\r\n" + 
						"FROM\r\n" + 
						"    scm_hdr hdr,\r\n" + 
						"    indent_hdr ihdr,\r\n" + 
						"    po_hdr po,\r\n" + 
						"    material_inward_hdr mih,\r\n" + 
						"    grn_hdr ghdr,\r\n" + 
						"    grn_dtl gdtl\r\n" + 
						"WHERE\r\n" + 
						"    hdr.PM_HDR_ID = ihdr.PROJECT_ID\r\n" + 
						"        AND ihdr.INDENT_ID = po.INDENT_ID\r\n" + 
						"        AND po.PO_ID = mih.PO_ID\r\n" + 
						"        AND ghdr.MI_ID = mih.MI_ID\r\n" + 
						"        and ghdr.GRN_HDR_ID=gdtl.GRN_HDR_ID\r\n" + 
						"        AND hdr.PM_HDR_ID = ? ";
				Map<String, Object> resultsData = jdbcTemplate.queryForMap(grnCountQry,obj.getPmHdrId());
				int grnCount = Integer.parseInt(resultsData.get("GRN_DTL_ID").toString());
				
				String statusQry = "SELECT\r\n"
						+ "   prh.PROJECT_CODE,prh.TRANSACTION_NO,prh.CUSTOMER_NAME,prh.PROJECT_NAME\r\n" + "FROM \r\n"
						+ "   project_hdr AS prh \r\n" + "WHERE\r\n" + "    prh.PM_HDR_ID = ? ";

				Map<String, Object> resultMapData = this.jdbcTemplate.queryForMap(statusQry, obj.getPmHdrId());
				
				ProjectHdrDtlEntity projectHdrDtlObj = new ProjectHdrDtlEntity();
				projectHdrDtlObj.setProjectCode(resultMapData.get("PROJECT_CODE").toString());
				projectHdrDtlObj.setTransactionNo(resultMapData.get("TRANSACTION_NO").toString());
				projectHdrDtlObj.setCustomerName(resultMapData.get("CUSTOMER_NAME").toString());
				projectHdrDtlObj.setProjectName(resultMapData.get("PROJECT_NAME").toString()); 
				
				scmHdrObj.setPmHdrId(obj.getPmHdrId());
				scmHdrObj.setScmHdrId(obj.getScmHdrId());
				scmHdrObj.setScmInitiatedDate(obj.getScmInitiatedDate());
				scmHdrObj.setPoCount(String.valueOf(poCount));
				scmHdrObj.setIntentCount(String.valueOf(intentCount));
				scmHdrObj.setHdrStatusDesc(obj.getHdrStatusDesc());
				scmHdrObj.setProjectCode(projectHdrDtlObj.getProjectCode());
				scmHdrObj.setTransactionStatus(obj.getTransactionStatus());
				scmHdrObj.setTransactionNo(projectHdrDtlObj.getTransactionNo());
				scmHdrObj.setSNo(obj.getSNo());
				scmHdrObj.setDueDate(obj.getDueDate());
				scmHdrObj.setTransactionStatusSeq(obj.getTransactionStatusSeq());
				scmHdrObj.setCustomerName(projectHdrDtlObj.getCustomerName());
				scmHdrObj.setProjectName(projectHdrDtlObj.getProjectName());
				scmHdrObj.setInwardCount(String.valueOf(inwardCount));
				scmHdrObj.setGrnCount(String.valueOf(grnCount));
				scmHdrObj.setEnquiryId(obj.getEnquiryId());
				scmHdrObj.setIsInternal(obj.getIsInternal());
				scmHdrBasedDtlList.add(scmHdrObj);
			}

		} catch (Exception ex) {
			logger.error("scmTitleViewList Method Exception --->" + ex);

		}
		return scmHdrBasedDtlList;
	}

	@Override
	public List<FinanceHdrEntity> financeTitleViewList(String fromDate, String toDate, String customer,
			String processId, String empId, String tenantID, String financeId, String projectId, String status) {
		logger.debug("getFinanceDtl DAO method start");
		List<FinanceHdrEntity> hdr = null;
		try {
			String custname = "";
			if (customer.equalsIgnoreCase("")) {
				custname = "%%";
			} else {
				custname = "%"+customer+"%" ;
			}

			String datediff="";
			if(!fromDate.equalsIgnoreCase("")) {
				datediff = "  fh.INITIATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' AND";
			}
			String pro = " and fh.PM_HDR_ID like '%%' ";
			if(!projectId.equalsIgnoreCase("getall")) {
				pro = " and fh.PM_HDR_ID = '"+projectId+"'";
			}
			
				String getQ = "SELECT \r\n" + 
						"    fh.FE_HDR_ID,\r\n" + 
						"    fh.PM_HDR_ID,\r\n" + 
						"    fh.PROJECT_NAME,\r\n" + 
						"    fh.PROJECT_DESCRIPTION,\r\n" + 
						"    proj.PROJECT_CODE,\r\n" + 
						"    fh.PRODUCT_DETAILS,\r\n" + 
						"    fh.CUSTOMER_NAME,\r\n" + 
						"    emp.EMPLOYEE_FIRSTNAME AS REQUESTED_BY,\r\n" + 
						"    fh.INITIATED_DATE,\r\n" + 
						"    stg.STG_DESC AS TRANSACTION_STAGE,\r\n" + 
						"    dst.DOCUMENT_STATUS_TYPE_DESCRIPTION AS TRANSACTION_STATUS,\r\n" + 
						"    sales.PROJECT_HANDOVER_DATE,sales.SE_ID,\r\n" + 
						"    proj.DUE_DATE, sales.IS_INTERNAL\r\n" + 
						"FROM\r\n" + 
						"    finance_hdr fh\r\n" + 
						"        INNER JOIN\r\n" + 
						"    document_status_type_code dst ON fh.TRANSACTION_STATUS = dst.DOCUMENT_STATUS_TYPE_CODE\r\n" + 
						"        INNER JOIN\r\n" + 
						"    stg_master stg ON fh.TRANSACTION_STAGE = stg.STG_CODE\r\n" + 
						"        INNER JOIN\r\n" + 
						"    employee_mst emp ON fh.REQUESTED_BY = emp.EMPLOYEE_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    project_hdr proj ON fh.PM_HDR_ID = proj.PM_HDR_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    process_assigned_team pat ON fh.FE_HDR_ID = pat.MASTER_ID\r\n" + 
						"        INNER JOIN\r\n" + 
						"    sales_enq_hdr sales ON proj.ENQUIRY_ID = sales.SE_ID\r\n" + 
						"WHERE\r\n" + datediff +
						"         ASSIGNED_EMP_ID = ?\r\n" + 
						"        AND fh.CUSTOMER_NAME LIKE '"+custname+"' \r\n" + 
						"        AND pat.PM_ID =? AND pat.TENANT_ID = fh.TENANT_ID \r\n" + 
						"        AND pat.IS_ACTIVE = 1 and proj.TRANSACTION_STATUS = '"+status+"' \r\n" + 
						"        AND fh.TENANT_ID = ?\r\n"+pro ;

				hdr = this.jdbcTemplate.query(getQ, new FinanceHdrRowMapper(),empId ,processId,tenantID);
			

		} catch (Exception ex) {
			logger.error("Error with method financeTitleViewList " + ex.getMessage());
		}
		logger.debug("financeTitleViewList DAO method end");
		return hdr;
	}

	@Override
	public List<GetAssyDtlEntity> assyTitleViewList(String fromDate, String toDate, String customerName, String assyHdrId,
			String tenantId, String pmId, String empId, String projectId, String status) {
		List<GetAssyDtlEntity> list = new ArrayList<GetAssyDtlEntity>();
		String datediff="";
		if(!fromDate.equalsIgnoreCase("")) {
			datediff = "AND hdr.REQUEST_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		String pro = " and hdr.PM_HDR_ID like '%%' ";
		if(!projectId.equalsIgnoreCase("getall")) {
			pro = " and hdr.PM_HDR_ID = '"+projectId+"'";
		}
		try {
				String custName = "%" + customerName + "%";
				String assyHdrStr = "SELECT \r\n" + "    hdr.*,phdr.PROJECT_CODE,phdr.ENQUIRY_ID, \r\n"
						+ "    stageDesc.STG_DESC AS STAGE_DESC,\r\n"
						+ "    statusDesc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS STATUS_DESC\r\n" + "FROM\r\n"
						+ "    assy_hdr hdr\r\n" + "        INNER JOIN\r\n"
						+ "    document_status_type_code statusDesc\r\n" + "        INNER JOIN\r\n"
						+ "    stg_master stageDesc,\r\n" + "    process_assigned_team pat ,  project_hdr phdr \r\n"
						+ "WHERE\r\n" + "    hdr.TRANSACTION_STAGE = stageDesc.STG_CODE\r\n"
						+ "        AND pat.MASTER_ID = hdr.ASSY_HDR_ID AND phdr.PM_HDR_ID = hdr.PM_HDR_ID \r\n"
						+ "        AND pat.ASSIGNED_EMP_ID = ? AND pat.TENANT_ID = hdr.TENANT_ID  \r\n"
						+ "        AND hdr.TRANSACTION_STATUS = statusDesc.DOCUMENT_STATUS_TYPE_CODE\r\n"
						+ "        \r\n"+datediff
						+ "        AND hdr.CUSTOMER_NAME LIKE ?\r\n" + "        AND hdr.TENANT_ID = ?\r\n"
						+ "        AND PM_ID = ? AND pat.IS_ACTIVE = 1 and hdr.TRANSACTION_STATUS = '"+status+"' "+pro ;
				list = this.jdbcTemplate.query(assyHdrStr, new GetAssyDtlRowMapper(), empId, custName,
						tenantId, pmId);
			
		} catch (Exception ex) {
			logger.error("assyTitleViewList Error" + ex);
		}
		return list;
	}

	@Override
	public List<GetQtyDtlEntity> getQtyDtltile(String qHdrId, String empId, String fromDate, String toDate, String tenantId,
			String customerName, String pmId, String projectId, String status) {
		List<GetQtyDtlEntity> qtyHdrList = new ArrayList<GetQtyDtlEntity>();
		try {
			String datediff="";
			if(!fromDate.equalsIgnoreCase("")) {
				datediff = "AND qhdr.INTIATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			}
			String pro = " and qhdr.PM_HDR_ID like '%%' ";
			if(!projectId.equalsIgnoreCase("getall")) {
				pro = " and qhdr.PM_HDR_ID = '"+projectId+"'";
			}
			if (qHdrId.isEmpty()) {
				String custName = "%" + customerName + "%";
				String assyHdrStr = "SELECT \r\n" +
						"    qhdr.*,\r\n" +
						"    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS STATUS_DESC,\r\n" +
						"    dstc2.STG_DESC AS STAGE_DESC,\r\n" +
						"    hdr.PROJECT_CODE,\r\n" +
						"    hdr.PROJECT_NAME,\r\n" +
						"    hdr.PROJECT_DESCRIPTION,hdr.CUSTOMER_NAME,hdr.ENQUIRY_ID \r\n" +
						"FROM\r\n" +
						"    quality_hdr qhdr,\r\n" +
						"    project_hdr hdr,\r\n" +
						"    document_status_type_code dstc,\r\n" + 
						"    stg_master dstc2,\r\n" + 
						"    process_assigned_team pat\r\n" + 
						"WHERE\r\n" + 
						"    hdr.PM_HDR_ID = qhdr.PM_HDR_ID AND hdr.TENANT_ID = qhdr.TENANT_ID AND pat.TENANT_ID = qhdr.TENANT_ID  \r\n" + 
						"        AND dstc.DOCUMENT_STATUS_TYPE_CODE = qhdr.TRANSACTION_STATUS\r\n" + 
						"        AND dstc2.STG_CODE = qhdr.TRANSACTION_STAGE\r\n" + datediff+
						"        AND hdr.CUSTOMER_NAME like ?\r\n" + 
						"     AND pat.MASTER_ID = qhdr.Q_HDR_ID    AND pat.PM_ID = ? \r\n" + 
						"        AND pat.ASSIGNED_EMP_ID = ? \r\n" + 
						"        AND qhdr.TENANT_ID = ?  AND pat.IS_ACTIVE = 1 and qhdr.TRANSACTION_STATUS='"+status+"' "+pro;
				qtyHdrList = this.jdbcTemplate.query(assyHdrStr, new GetQtyDtlRowMapper(),custName,pmId,empId,
						tenantId);
			} 
		} catch (Exception ex) {
			logger.error("getQtyDtltile Error" + ex);
		}
		return qtyHdrList;
	}


	
}
