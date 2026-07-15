package com.vmfg.mis.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.mis.dao.interfaces.IManagementMisDAO;
import com.vmfg.mis.entity.OverAllProjSpentDrillDownEntity;
import com.vmfg.mis.entity.ProjConsumedValEntity;
import com.vmfg.mis.entity.ProjDetailsDrillDownEntity;
import com.vmfg.mis.entity.ProjSpentDrillDownEntity;
import com.vmfg.mis.entity.ProjectCntlEntity;
import com.vmfg.mis.entity.VendorDetailDrillDownEntity;
import com.vmfg.mis.rowmapper.GetOverAllProjSpentDrillDownRowMapper;
import com.vmfg.mis.rowmapper.GetProjConsumedValRowMapper;
import com.vmfg.mis.rowmapper.GetProjDetailsDrillDownRowMapper;
import com.vmfg.mis.rowmapper.GetProjSpentDrillDownRowMapper;
import com.vmfg.mis.rowmapper.GetTotalProjectCntRowMapper;
import com.vmfg.mis.rowmapper.GetVendorDetailDrillDownRowMapper;

@Transactional
@Repository
public class ManagementMisDAO implements IManagementMisDAO {
	private static final Logger logger = LoggerFactory.getLogger(ManagementMisDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<ProjectCntlEntity> getTotalProjCnt(String tenantId, String fromDate, String toDate, String stageCode,
			String custCode, String pmHdrId) {
		List<ProjectCntlEntity> list = new ArrayList<ProjectCntlEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \r\n" + 
	    			"    count(*) AS CNT, SUM(TOTAL_BUDGET_COST + CR_COST + SALE_PERCENT + CR_SALE_PERCENT) AS PROJECT_VALUE, \r\n" + 
	    			"    SUM(sbHdr.SALE_PERCENT + sbHdr.CR_SALE_PERCENT) AS CONTRIBUTION_VALUE\r\n" + 
	    			"FROM\r\n" + 
	    			"    project_hdr hdr inner join customer_mst mst \r\n" + 
	    			"    on mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"    inner join sales_budget_sheet_hdr sbHdr\r\n" + 
	    			"    on sbHdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" + 
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+date+"";
	    	list = this.jdbcTemplate.query(Qry, new GetTotalProjectCntRowMapper());
	    }catch(Exception ex) {
	    	logger.error("getTotalProjCnt DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<ProjConsumedValEntity> getProjConsumedValue(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId) {
		List<ProjConsumedValEntity> list = new ArrayList<ProjConsumedValEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = " SELECT \r\n" + 
	    			"    (SELECT SUM(BUDGET_VALUE) \r\n" +
	    			" from \r\n" + 
	    			"	  indent_hdr ihdr inner join project_hdr hdr ON ihdr.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
	    			"	 inner join customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME \r\n" + 
	    			"	   WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"	    		   "+pmHdr+" \r\n" + 
	    			"	    		   "+stage+" \r\n" + 
	    			"	    		   "+cust+"  \r\n" + 
	    			"	    		   "+date+" \r\n " +
	    		    ") as BUDGET_VALUE, \r\n" +
	    		    "    (select SUM(BASIC_TOTAL) from \r\n" +
	    		    " po_hdr phdr inner join indent_hdr ihdr ON phdr.INDENT_ID = ihdr.INDENT_ID \r\n" +
	    		    " and phdr.IS_LATEST = 1 inner join project_hdr hdr ON ihdr.PROJECT_ID = hdr.PM_HDR_ID \r\n" +
	    		    " inner join customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME \r\n" +
	    		    " WHERE hdr.TENANT_ID = '"+tenantId+"'  \r\n" + 
	    		    "	    			"+pmHdr+" \r\n" + 
	    		    "	    			"+stage+" \r\n" + 
	    		    "	    			"+cust+" \r\n" + 
	    		    "	    		    "+date+") as BASIC_TOTAL, \r\n" +
	    		    "  SUM(TOTAL_BUDGET_COST + CR_COST) as PROJECT_BUDGET \r\n" + 
	    			"FROM\r\n" + 
	    			"    project_hdr hdr\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    sales_budget_sheet_hdr sbHdr ON sbHdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" + 
//	    			"        LEFT JOIN\r\n" + 
//	    			"    indent_hdr ihdr ON ihdr.PROJECT_ID = hdr.PM_HDR_ID\r\n" +
//	    			"        LEFT JOIN \r\n" +
//	    			"    po_hdr phdr ON phdr.INDENT_ID = ihdr.INDENT_ID and phdr.IS_LATEST = 1 \r\n" + 
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+date+"";
	    	list = this.jdbcTemplate.query(Qry, new GetProjConsumedValRowMapper());
			 if(list.size()>0) {
			    		String qry ="SELECT \r\n" + 
			    				"    COALESCE(inv.total_cost, 0) +\r\n" + 
			    				"    COALESCE(indent.cash_voucher, 0) \r\n" + 
			    				"   + COALESCE(timesheet.timesheet_cost, 0) \r\n" +
			    			    " - COALESCE(debit_value.debit_val, 0) AS ACTUAL_SPENT\r\n" + 
			    				"FROM\r\n" + 
			    				"    (SELECT SUM(OVER_ALL_COST) AS total_cost\r\n" + 
			    				"     FROM inventroy_material_transfer imt \r\n" + 
			    				"	 INNER JOIN project_hdr hdr on imt.TO_PM_HDR_ID = hdr.PM_HDR_ID AND FROM_PM_HDR_ID != TO_PM_HDR_ID \r\n" + 
			    				"     INNER JOIN customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
			    				"     WHERE hdr.TENANT_ID = '"+tenantId+"'  \r\n" + 
			    				"	    			"+pmHdr+"\r\n" + 
			    				"	    			"+stage+"  \r\n" + 
			    				"	    		    "+cust+" \r\n" + 
			    				"	    			"+date+" \r\n" + 
			    				"     ) AS inv,     \r\n" + 
			    				"    (SELECT SUM(CASE\r\n" + 
//			    				"                WHEN VENDOR_QUALIFIED = 'L1' THEN L1_FINAL_SUB_TOTAL\r\n" + 
//			    				"                WHEN VENDOR_QUALIFIED = 'L2' THEN L2_FINAL_SUB_TOTAL\r\n" + 
//			    				"                WHEN VENDOR_QUALIFIED = 'L3' THEN L3_FINAL_SUB_TOTAL\r\n" + 
			    				"                WHEN SCM_BUDGET_ALLOCATED > 0 THEN SCM_BUDGET_ALLOCATED ELSE 0\r\n" + 
			    				"            END) AS cash_voucher\r\n" + 
			    				"     FROM indent_hdr ihdr\r\n" + 
//			    				"     INNER JOIN indent_grp_scs scs ON scs.INDENT_ID = ihdr.INDENT_ID\r\n" + 
//			    				"     INNER JOIN indent_grp_scs_ven_dtl ivdtl ON ivdtl.IG_SCS_ID = scs.IG_SCS_ID\r\n" + 
			    				"     INNER JOIN project_hdr hdr on ihdr.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
			    				"     INNER JOIN customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
			    				"     WHERE hdr.TENANT_ID = '"+tenantId+"'  \r\n" + 
			    				"	    			"+pmHdr+"\r\n" + 
			    				"	    			"+stage+"  \r\n" + 
			    				"	    		    "+cust+" \r\n" + 
			    				"	    			"+date+" \r\n" +  
			    				"     ) AS indent,\r\n"  +
			    				"    (SELECT SUM(TIMESHEET_COST) AS timesheet_cost\r\n" + 
			    				"     FROM timesheet_hdr th\r\n" + 
			    				"     INNER JOIN timesheet_dtl td ON td.T_HDR_ID = th.T_HDR_ID\r\n" + 
			    				"     INNER JOIN project_hdr hdr on th.PM_HDR_ID = hdr.PM_HDR_ID \r\n" + 
			    				"     INNER JOIN customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
			    				"     WHERE hdr.TENANT_ID = '"+tenantId+"'  \r\n" + 
			    				"	    			"+pmHdr+"\r\n" + 
			    				"	    			"+stage+"  \r\n" + 
			    				"	    		    "+cust+" \r\n" + 
			    				"	    			"+date+" \r\n" + 
			    				"     ) AS timesheet, \r\n" 
			    				+ "  (Select sum(DN_VALUE) AS debit_val from debit_note note inner join \r\n"
			    				+ "   project_hdr hdr on note.PM_HDR_ID = hdr.PM_HDR_ID \r\n"
			    				+ "   INNER JOIN customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME"
			    				+ " WHERE hdr.TENANT_ID = '"+tenantId+"'  \r\n" + 
			    				"			    "+pmHdr+" \r\n" + 
			    				"			    "+stage+"   \r\n" + 
			    				"			    "+cust+" \r\n" + 
			    				"			    "+date+" ) AS debit_value ";
			    		Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry);
		   			    String actualCost = resultMap.get("ACTUAL_SPENT") != null ? resultMap.get("ACTUAL_SPENT").toString() : "0";
		   			    list.get(0).setActualSpend(actualCost);
				 String actual = list.get(0).getActualSpend();
				 String BudgetVal = list.get(0).getProjBudget();
				 list.get(0).setBalanceAvailable(String.valueOf(new BigDecimal(BudgetVal).subtract(new BigDecimal(actual))));
			 }
	    }catch(Exception ex) {
	    	logger.error("getProjConsumedValue DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<ProjSpentDrillDownEntity> getProjSpentDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId) {
		List<ProjSpentDrillDownEntity> list = new ArrayList<ProjSpentDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = " SELECT \r\n" + 
	    			"    PROJECT_CODE,\r\n" + 
	    			"    hdr.PM_HDR_ID,\r\n" + 
	    			"    PROJECT_NAME,\r\n" + 
	    			"    SUM(BUDGET_VALUE) AS BUDGET_VALUE, \r\n" +
	    		    "    SUM(SCM_BUDGET_ALLOCATED) AS SCM_BUDGET_ALLOCATED,\r\n" + 
	    			"    (SELECT \r\n" + 
	    			"            SUM(BUDGET_VALUE)\r\n" + 
	    			"        FROM\r\n" + 
	    			"            indent_hdr\r\n" + 
	    			"        WHERE\r\n" + 
	    			"            PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"                AND INDENT_TYPE_CODE = 'IT001') AS MATERIAL_VALUE,\r\n" + 
	    			"    (SELECT \r\n" + 
	    			"            SUM(BUDGET_VALUE)\r\n" + 
	    			"        FROM\r\n" + 
	    			"            indent_hdr\r\n" + 
	    			"        WHERE\r\n" + 
	    			"            PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"                AND INDENT_TYPE_CODE = 'IT002') AS SERVICE_VALUE,\r\n" + 
	    			"    (SELECT \r\n" + 
	    			"            SUM(BASIC_TOTAL)\r\n" + 
	    			"        FROM\r\n" + 
	    			"            po_hdr phdr\r\n" + 
	    			"                INNER JOIN\r\n" + 
	    			"            indent_hdr ih ON ih.INDENT_ID = phdr.INDENT_ID\r\n" + 
	    			"        WHERE\r\n" + 
	    			"            PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"                AND INDENT_TYPE_CODE = 'IT001' AND IS_LATEST = 1\r\n" + 
//	    			"                #AND IS_APPROVED = 1  \r\n" + 
	    			"               ) AS MATERIAL_PO_RELEASED,\r\n" + 
	    			"    (SELECT \r\n" + 
	    			"            SUM(BASIC_TOTAL)\r\n" + 
	    			"        FROM\r\n" + 
	    			"            po_hdr phdr\r\n" + 
	    			"                INNER JOIN\r\n" + 
	    			"            indent_hdr ih ON ih.INDENT_ID = phdr.INDENT_ID\r\n" + 
	    			"        WHERE\r\n" + 
	    			"            PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"                AND INDENT_TYPE_CODE = 'IT002' AND IS_LATEST = 1\r\n" + 
//	    			"                AND IS_APPROVED = 1  \r\n" + 
	    			"               ) AS SERVICE_PO_RELEASED,\r\n" + 
	    			"    SUM(BUDGET_VALUE) - (SELECT \r\n" + 
	    			"            SUM(BASIC_TOTAL)\r\n" + 
	    			"        FROM\r\n" + 
	    			"            po_hdr phdr\r\n" + 
	    			"                INNER JOIN\r\n" + 
	    			"            indent_hdr ih ON ih.INDENT_ID = phdr.INDENT_ID\r\n" + 
	    			"        WHERE\r\n" + 
	    			"            PROJECT_ID = hdr.PM_HDR_ID AND IS_LATEST = 1\r\n" + 
//	    			"                AND IS_APPROVED = 1  \r\n" + 
	    			"               ) AS BALANCE\r\n" + 
	    			"FROM\r\n" + 
	    			"    project_hdr hdr\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    sales_budget_sheet_hdr sbHdr ON sbHdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" + 
	    			"        LEFT JOIN\r\n" + 
	    			"    indent_hdr ihdr ON ihdr.PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+date+"\r\n" +
	    			"GROUP BY hdr.PM_HDR_ID order by hdr.PROJECT_CODE";
	    	list = this.jdbcTemplate.query(Qry, new GetProjSpentDrillDownRowMapper());
	     	
	    }catch(Exception ex) {
	    	logger.error("getProjSpentDrillDown DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<OverAllProjSpentDrillDownEntity> getOverAllProjSpentDrillDown(String tenantId, String fromDate,
			String toDate, String stageCode, String custCode, String pmHdrId) {
		List<OverAllProjSpentDrillDownEntity> list = new ArrayList<OverAllProjSpentDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \r\n" + 
	    			"    PROJECT_CODE,\r\n" + 
	    			"    hdr.PM_HDR_ID,\r\n" + 
	    			"    PROJECT_NAME,\r\n" + 
	    			"    (TOTAL_BUDGET_COST + CR_COST) AS SALE_BUDGET_VALUE,\r\n" + 
	    			"    SUM(BUDGET_VALUE) AS BUDGET_VALUE,\r\n" + 
	    			"    CUSTOMER_NAME,\r\n" + 
	    			"    DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
	    			"    (SELECT \r\n" + 
	    			"            SUM(BASIC_TOTAL)\r\n" + 
	    			"        FROM\r\n" + 
	    			"            po_hdr phdr\r\n" + 
	    			"                INNER JOIN\r\n" + 
	    			"            indent_hdr ih ON ih.INDENT_ID = phdr.INDENT_ID\r\n" + 
//	    			"                INNER JOIN\r\n" + 
//	    			"            indent_grp_scs scs ON scs.INDENT_ID = ih.INDENT_ID\r\n" + 
	    			"        WHERE\r\n" + 
	    			"            PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"                 AND phdr.IS_LATEST = 1) AS PO_RELEASED,\r\n" + 
	    			"    (SELECT \r\n" + 
	    			"            SUM(TIMESHEET_COST)\r\n" + 
	    			"        FROM\r\n" + 
	    			"            timesheet_hdr th\r\n" + 
	    			"                INNER JOIN\r\n" + 
	    			"            timesheet_dtl td ON td.T_HDR_ID = th.T_HDR_ID\r\n" + 
	    			"        WHERE\r\n" + 
	    			"            PM_HDR_ID = hdr.PM_HDR_ID) AS TIMESHEET_COST,\r\n" + 
	    			"    (SELECT \r\n" + 
	    			"            SUM(OVER_ALL_COST)\r\n" + 
	    			"        FROM\r\n" + 
	    			"            inventroy_material_transfer\r\n" + 
	    			"        WHERE\r\n" + 
	    			"            TO_PM_HDR_ID = hdr.PM_HDR_ID) AS OVER_ALL_COST, \r\n" +
	    		    "      (SELECT \r\n" + 
	    		    "    SUM(PAYABLE_OPEN_BAL - PAYABLE_PENDING_BAL)\r\n" + 
	    		    "FROM\r\n" + 
	    		    "    payable_billing_dtl pbd\r\n" + 
	    		    "        INNER JOIN\r\n" + 
	    		    "    project_hdr phdr ON phdr.PROJECT_CODE = pbd.PRA_CODE\r\n" + 
	    		    "WHERE phdr.PM_HDR_ID =  hdr.PM_HDR_ID) as OTHERS_TALLY\r\n" + 
	    			"FROM\r\n" + 
	    			"    project_hdr hdr\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    sales_budget_sheet_hdr sbHdr ON sbHdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" + 
	    			"        LEFT JOIN\r\n" + 
	    			"    indent_hdr ihdr ON ihdr.PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    document_status_type_code dtc ON dtc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\r\n" + 
	    			"WHERE\r\n" + 
	    			"    hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+date+"\r\n" +
	    			"GROUP BY hdr.PM_HDR_ID order by hdr.PROJECT_CODE";
	    	list = this.jdbcTemplate.query(Qry, new GetOverAllProjSpentDrillDownRowMapper());
	    	
//	    	if(list.size()>0) {
//	    		for(OverAllProjSpentDrillDownEntity proj : list) {
//	    			String qry = "SELECT \r\n" + 
//	    					"  SUM(\r\n" + 
//	    					"    CASE \r\n" + 
//	    					"      WHEN VENDOR_QUALIFIED = 'L1' THEN L1_FINAL_SUB_TOTAL\r\n" + 
//	    					"      WHEN VENDOR_QUALIFIED = 'L2' THEN L2_FINAL_SUB_TOTAL\r\n" + 
//	    					"      WHEN VENDOR_QUALIFIED = 'L3' THEN L3_FINAL_SUB_TOTAL\r\n" + 
//	    					"      ELSE 0\r\n" + 
//	    					"    END\r\n" + 
//	    					"  ) AS CASH_VOUCHER\r\n" + 
//	    					"FROM indent_hdr hdr\r\n" + 
//	    					"INNER JOIN indent_grp_scs scs ON scs.INDENT_ID = hdr.INDENT_ID\r\n" + 
//	    					"INNER JOIN indent_grp_scs_ven_dtl ivdtl ON ivdtl.IG_SCS_ID = scs.IG_SCS_ID\r\n" + 
//	    					"WHERE TYPE = 'Cash Voucher' AND PROJECT_ID = '"+proj.getPmHdrId()+"';";
//	    			
//	    			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry);
//	   			    String cashVal = resultMap.get("CASH_VOUCHER") != null ? resultMap.get("CASH_VOUCHER").toString() : "0";
////	   			    proj.setCashVouchar(cashVal);
//	    		}
//	    	}
	     	
	    }catch(Exception ex) {
	    	logger.error("getOverAllProjSpentDrillDown DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<OverAllProjSpentDrillDownEntity> getProjActualValDrillDown(String tenantId, String fromDate,
			String toDate, String stageCode, String custCode, String pmHdrId) {
		List<OverAllProjSpentDrillDownEntity> list = new ArrayList<OverAllProjSpentDrillDownEntity>();
//		List<ProjSpentDrillDownEntity> subList = null;
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \n" +
					"    PROJECT_CODE,\n" +
					"    hdr.PM_HDR_ID,\n" +
					"    (TOTAL_BUDGET_COST + CR_COST + SALE_PERCENT + CR_SALE_PERCENT) AS ORDER_VALUE,\n" +
					"    PROJECT_NAME,\n" +
					"    (TOTAL_BUDGET_COST + CR_COST) AS SALE_BUDGET_VALUE,\n" +
					"    (SELECT \n" +
					"            SUM(COALESCE(TIMESHEET_COST, 0))\n" +
					"        FROM\n" +
					"            timesheet_hdr th\n" +
					"                INNER JOIN\n" +
					"            timesheet_dtl td ON td.T_HDR_ID = th.T_HDR_ID\n" +
					"        WHERE\n" +
					"            PM_HDR_ID = hdr.PM_HDR_ID) AS TIMESHEET_COST\n" +
					"FROM\n" +
					"    project_hdr hdr\n" +
					"        INNER JOIN\n" +
					"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\n" +
					"        INNER JOIN\n" +
					"    sales_budget_sheet_hdr sbHdr ON sbHdr.MASTER_ID = hdr.ENQUIRY_ID\n" +
					"WHERE\n" +
					"    hdr.TENANT_ID = '"+tenantId+"' \r\n" +
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+date+"\r\n" +
	    			"GROUP BY hdr.PM_HDR_ID order by hdr.PROJECT_CODE";
	    	list = this.jdbcTemplate.query(Qry, new GetOverAllProjSpentDrillDownRowMapper());
	    	if(list.size()>0) {
//	    		for( OverAllProjSpentDrillDownEntity obj : list ) {
	    		getProjSpentDrillDown(tenantId, fromDate, toDate, stageCode, custCode, pmHdrId);
		    		
//	    		}
	    		
	    	}
	     	
	    }catch(Exception ex) {
	    	logger.error("getProjActualValDrillDown DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<ProjDetailsDrillDownEntity> getProjDetailsDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId) {
		List<ProjDetailsDrillDownEntity> list = new ArrayList<ProjDetailsDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \r\n" + 
	    			"    hdr.PROJECT_CODE, mst.CUST_NAME, dsc.DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
	    			"    hdr.PM_HDR_ID, (TOTAL_BUDGET_COST + CR_COST + SALE_PERCENT + CR_SALE_PERCENT) as ORDER_VALUE,\r\n" + 
	    			"    PROJECT_NAME, (TOTAL_BUDGET_COST + CR_COST) as SALE_BUDGET_VALUE,\r\n" + 
	    			"    (CR_SALE_PERCENT+SALE_PERCENT) AS CONTRIBUTION, \r\n" + 
	    			"    sbHdr.RECEIVED_BALANCE AS RECEIVED_BALANCE,\r\n" + 
	    			"    (TOTAL_BUDGET_COST + CR_COST + SALE_PERCENT + CR_SALE_PERCENT) - (sbHdr.RECEIVED_BALANCE) AS RECEIVABLE\r\n" + 
	    			"FROM\r\n" + 
	    			"    project_hdr hdr\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    sales_budget_sheet_hdr sbHdr ON sbHdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" + 
	    			"        LEFT JOIN\r\n" + 
	    			"    indent_hdr ihdr ON ihdr.PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"		LEFT JOIN\r\n" + 
	    			"	document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\r\n" + 
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+date+"\r\n" +
	    			"GROUP BY hdr.PM_HDR_ID order by hdr.PROJECT_CODE\r\n";
	    	list = this.jdbcTemplate.query(Qry, new GetProjDetailsDrillDownRowMapper());
	    	if(list.size()>0) {
	    		for(ProjDetailsDrillDownEntity proj : list ) {
	    			String qry ="SELECT\r\n" + 
	    					"    ((TOTAL_BUDGET_COST + CR_COST + SALE_PERCENT + CR_SALE_PERCENT) / 100\r\n" + 
	    					"    * SUM(CASE\r\n" + 
	    					"        WHEN CURDATE() < DATE(pt.ACTUAL_DATE) THEN 0\r\n" + 
	    					"        ELSE percentage\r\n" + 
	    					"    END)) as OUTSTANDING\r\n" + 
	    					"FROM\r\n" + 
	    					"    project_hdr hdr\r\n" + 
	    					"INNER JOIN\r\n" + 
	    					"    sales_budget_sheet_hdr sbHdr ON sbHdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" + 
	    					"LEFT JOIN\r\n" + 
	    					"     budget_sheet_payment_terms pt ON sbHdr.SB_HDR_ID = pt.BS_HDR_ID\r\n" + 
	    					"WHERE\r\n" + 
	    					"    hdr.PM_HDR_ID = '"+proj.getPmHdrId()+"';";
	    			Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry);
	   			    String outStCost = resultMap.get("OUTSTANDING") != null ? resultMap.get("OUTSTANDING").toString() : "0";
	   			    proj.setOutstanding(outStCost);
	    		}
	    	}
	    }catch(Exception ex) {
	    	logger.error("getProjDetailsDrillDown DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<VendorDetailDrillDownEntity> getVendorDetailDrillDown(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId) {
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "" , vendor = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' and '"+toDate+"' ";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		vendor = "getall".equalsIgnoreCase(vendorId) ? "AND vmst.VENDOR_CODE like '%%'" : " AND vmst.VENDOR_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \r\n" +
	    			"    PROJECT_CODE, hdr.PM_HDR_ID,\r\n" +
	    			"    PROJECT_NAME,\r\n" +
	    			"    vmst.VENDOR_NAME,\r\n" +
	    			"    phdr.PO_CODE,\r\n" +
	    			"    pra.PO_DATE, pra.PRA_CODE, pra.PRA_DATE,\r\n" +
	    			"    pra.AMOUNT_PAYABLE, pra.AMOUNT_DUE,\r\n" +
	    			"    COALESCE(mi.DC_NO, (SELECT DC_NO FROM material_inward_hdr WHERE PO_ID = pra.PO_ID AND TENANT_ID = '"+tenantId+"' ORDER BY MI_ID DESC LIMIT 1)) AS INVOICE_NO,\r\n" +
	    			"    COALESCE(mi.DC_DATE, (SELECT DC_DATE FROM material_inward_hdr WHERE PO_ID = pra.PO_ID AND TENANT_ID = '"+tenantId+"' ORDER BY MI_ID DESC LIMIT 1)) AS INVOICE_DATE,\r\n" +
	    			"    pra.DUE_DATE, (SELECT DOCUMENT_STATUS_TYPE_DESCRIPTION FROM pra_hdr hdr inner join document_status_type_code mst\r\n" +
	    			"    on hdr.STATUS_CODE = mst.DOCUMENT_STATUS_TYPE_CODE where hdr.PRA_ID = pra.PRA_ID and hdr.TENANT_ID='"+tenantId+"') as STATUS,\r\n" +
	    			"    CASE WHEN pra.COMPLETED_DATETIME is not null THEN  DATEDIFF(DATE(pra.COMPLETED_DATETIME),DATE(pra.DUE_DATE))\r\n" +
	    			"	    ELSE (CASE WHEN CURDATE() < DATE(pra.DUE_DATE) THEN 0 ELSE DATEDIFF(CURDATE(), DATE(pra.DUE_DATE)) END)\r\n" +
	    			"	 END AS OVER_DUE \r\n" +
	    			"FROM\r\n" +
	    			"    project_hdr hdr\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    indent_hdr ih ON ih.PROJECT_ID = hdr.PM_HDR_ID\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    po_hdr phdr ON ih.INDENT_ID = phdr.INDENT_ID\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    pra_hdr pra ON pra.PO_ID = phdr.PO_ID and pra.TENANT_ID = '"+tenantId+"'\r\n" +
	    			"        INNER JOIN\r\n" +
	    			"    vendor_mst vmst ON vmst.VENDOR_CODE = phdr.VENDOR_CODE\r\n" +
	    			"        LEFT JOIN\r\n" +
	    			"    grn_hdr gh ON gh.GRN_HDR_ID = pra.GRN_HDR_ID\r\n" +
	    			"        LEFT JOIN\r\n" +
	    			"    material_inward_hdr mi ON mi.MI_ID = gh.MI_ID\r\n" +
	    			"        LEFT JOIN\r\n" +
	    			"	document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\r\n" +
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" +
	    			"		"+pmHdr+"\r\n" +
	    			"       "+stage+"\r\n" +
	    			"       "+cust+"\r\n" +
	    			"       "+vendor+"\r\n" +
	    			"       "+date+"\r\n";
	    	list = this.jdbcTemplate.query(Qry, new GetVendorDetailDrillDownRowMapper());
	    }catch(Exception ex) {
	    	logger.error("getVendorDetailDrillDown DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<VendorDetailDrillDownEntity> getVendorDetailHdrView(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId) {
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "", vendor="";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' and '"+toDate+"' ";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		vendor = "getall".equalsIgnoreCase(vendorId) ? "AND vmst.VENDOR_CODE like '%%'" : " AND vmst.VENDOR_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \r\n" + 
	    			"    PROJECT_CODE, hdr.PM_HDR_ID, vmst.VENDOR_CODE,\r\n" + 
	    			"    PROJECT_NAME,\r\n" + 
	    			"    vmst.VENDOR_NAME,\r\n" + 
	    			"    SUM(AMOUNT_PAYABLE) AS AMOUNT_PAYABLE, SUM(AMOUNT_DUE) as AMOUNT_DUE\r\n" + 
	    			"FROM\r\n" + 
	    			"    project_hdr hdr\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    indent_hdr ih ON ih.PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    po_hdr phdr ON ih.INDENT_ID = phdr.INDENT_ID\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    pra_hdr pra ON pra.PO_ID = phdr.PO_ID and pra.TENANT_ID = '"+tenantId+"'\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    vendor_mst vmst ON vmst.VENDOR_CODE = phdr.VENDOR_CODE\r\n" + 
	    			"        LEFT JOIN\r\n" + 
	    			"	document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\r\n" + 
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+vendor+"\r\n" + 
	    			"       "+date+"\r\n" +
	    			"group by vmst.VENDOR_NAME ";
	    	list = this.jdbcTemplate.query(Qry, new GetVendorDetailDrillDownRowMapper());
	    }catch(Exception ex) {
	    	logger.error("getVendorDetailHdrView DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<VendorDetailDrillDownEntity> getVendorPaymentDetails(String tenantId, String fromDate, String toDate,
			String stageCode, String custCode, String pmHdrId, String vendorId) {
		List<VendorDetailDrillDownEntity> list = new ArrayList<VendorDetailDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "", vendor = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' and '"+toDate+"' ";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		vendor = "getall".equalsIgnoreCase(vendorId) ? "AND vmst.VENDOR_CODE like '%%'" : " AND vmst.VENDOR_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT  \r\n" + 
//	    			"				(SELECT SUM(AMOUNT_PAYABLE) FROM pra_hdr WHERE STATUS = '9') as DONE_SO_FAR,\r\n" + 
	    			"                SUM(AMOUNT_DUE) as AMOUNT_DUE \r\n" + 
	    			"	    			FROM \r\n" + 
	    			"	    			    project_hdr hdr \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"                       customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"                           INNER JOIN\r\n" + 
	    			"	    			    indent_hdr ih ON ih.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"	    			    po_hdr phdr ON ih.INDENT_ID = phdr.INDENT_ID \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"	    			    pra_hdr pra ON pra.PO_ID = phdr.PO_ID and pra.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"	    			        INNER JOIN \r\n" + 
	    			"	    			    vendor_mst vmst ON vmst.VENDOR_CODE = phdr.VENDOR_CODE \r\n" + 
	    			"	    			        LEFT JOIN \r\n" + 
	    			"	    				document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS \r\n" +  
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+vendor+"\r\n" + 
	    			"       "+date+"\r\n";
	    	list = this.jdbcTemplate.query(Qry, new GetVendorDetailDrillDownRowMapper());
	    	if(list.size()>0) {
	    		String qry = "SELECT  \r\n" + 
		    			"				  SUM(AMOUNT_PAYABLE) as DONE_SO_FAR\r\n" + 
		    			"	    			FROM \r\n" + 
		    			"	    			    project_hdr hdr \r\n" + 
		    			"                           INNER JOIN\r\n" + 
		    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    indent_hdr ih ON ih.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    po_hdr phdr ON ih.INDENT_ID = phdr.INDENT_ID \r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    pra_hdr pra ON pra.PO_ID = phdr.PO_ID and pra.TENANT_ID = '"+tenantId+"' \r\n" + 
		    			"	    			        INNER JOIN \r\n" + 
		    			"	    			    vendor_mst vmst ON vmst.VENDOR_CODE = phdr.VENDOR_CODE \r\n" + 
		    			"	    			        LEFT JOIN \r\n" + 
		    			"	    				document_status_type_code dsc ON dsc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS \r\n" +  
		    			"WHERE hdr.TENANT_ID = '"+tenantId+"' and pra.STATUS = '9' \r\n" + 
		    			"		"+pmHdr+"\r\n" + 
		    			"       "+stage+"\r\n" + 
		    			"       "+cust+"\r\n" + 
		    			"       "+vendor+"\r\n" + 
		    			"       "+date+"\r\n";
	    		Map<String, Object> resultMap = jdbcTemplate.queryForMap(qry);
   			    String paidSoFar = resultMap.get("DONE_SO_FAR") != null ? resultMap.get("DONE_SO_FAR").toString() : "0";
   			    list.get(0).setPaidSoFar(paidSoFar);
	    	}
	    }catch(Exception ex) {
	    	logger.error("getVendorPaymentDetails DAO Error" + ex);	
	    }
		return list;
	}

	@Override
	public List<OverAllProjSpentDrillDownEntity> getProjSpentDetailByPmId(String tenantId, String fromDate,
			String toDate, String stageCode, String custCode, String pmHdrId) {
		List<OverAllProjSpentDrillDownEntity> list = new ArrayList<OverAllProjSpentDrillDownEntity>();
		String date = ""; String cust = ""; String stage = ""; String pmHdr = "";
		if(!fromDate.equalsIgnoreCase("")) {
			 date = "AND hdr.CREATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
		}
		cust = "getall".equalsIgnoreCase(custCode) ? "AND mst.CUST_CODE like '%%'" : " AND mst.CUST_CODE = '" + custCode + "'";
		stage = "getall".equalsIgnoreCase(stageCode) ? "AND hdr.TRANSACTION_STATUS like '%%'" : "AND hdr.TRANSACTION_STATUS = '"+stageCode+"'";
		pmHdr = "getall".equalsIgnoreCase(pmHdrId) ? "AND hdr.PM_HDR_ID like '%%'" : "AND hdr.PM_HDR_ID = '"+pmHdrId+"'";
	    try {
	    	String Qry = "SELECT \r\n" + 
	    			"    hdr.PROJECT_CODE, \r\n" + 
	    			"    hdr.PROJECT_NAME, CUSTOMER_NAME,\r\n" + 
	    			"    DOCUMENT_STATUS_TYPE_DESCRIPTION,\r\n" + 
	    			"    (TOTAL_BUDGET_COST + CR_COST + SALE_PERCENT + CR_SALE_PERCENT) AS ORDER_VALUE,\r\n" + 
	    			"    (TOTAL_BUDGET_COST + CR_COST) AS PROJECT_VALUE,\r\n" + 
	    			"\r\n" + 
	    			"    -- Material indent value\r\n" + 
	    			"    (SELECT SUM(BUDGET_VALUE) \r\n" + 
	    			"     FROM indent_hdr \r\n" + 
	    			"     WHERE PROJECT_ID = hdr.PM_HDR_ID AND INDENT_TYPE_CODE = 'IT001') AS MATERIAL_VALUE,\r\n" + 
	    			"\r\n" + 
	    			"    -- Service indent value\r\n" + 
	    			"    (SELECT SUM(BUDGET_VALUE) \r\n" + 
	    			"     FROM indent_hdr \r\n" + 
	    			"     WHERE PROJECT_ID = hdr.PM_HDR_ID AND INDENT_TYPE_CODE IN ('IT002','IT003')) AS SERVICE_VALUE,\r\n" + 
	    			"\r\n" + 
	    			"    -- Total indent value\r\n" + 
	    			"    (SELECT SUM(BUDGET_VALUE) \r\n" + 
	    			"     FROM indent_hdr \r\n" + 
	    			"     WHERE PROJECT_ID = hdr.PM_HDR_ID AND INDENT_TYPE_CODE IN ('IT001', 'IT002', 'IT003')) AS TOTAL_VALUE,\r\n" + 
	    			"\r\n" + 
	    			"    -- Material PO released\r\n" + 
	    			"    (SELECT SUM(BASIC_TOTAL) \r\n" + 
	    			"     FROM po_hdr phdr \r\n" + 
	    			"     JOIN indent_hdr ih ON ih.INDENT_ID = phdr.INDENT_ID \r\n" + 
	    			"     WHERE ih.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
	    			"       AND ih.INDENT_TYPE_CODE = 'IT001' \r\n" + 
	    			"       AND phdr.IS_LATEST = 1) AS MATERIAL_PO_RELEASED,\r\n" + 
	    			"\r\n" + 
	    			"    -- Service PO released\r\n" + 
	    			"    (SELECT SUM(BASIC_TOTAL) \r\n" + 
	    			"     FROM po_hdr phdr \r\n" + 
	    			"     JOIN indent_hdr ih ON ih.INDENT_ID = phdr.INDENT_ID \r\n" + 
	    			"     WHERE ih.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
	    			"       AND ih.INDENT_TYPE_CODE IN ('IT002','IT003') \r\n" + 
	    			"       AND phdr.IS_LATEST = 1) AS SERVICE_PO_RELEASED,\r\n" + 
	    			"\r\n" + 
	    			"    -- Total PO released\r\n" + 
	    			"    (SELECT SUM(BASIC_TOTAL) \r\n" + 
	    			"     FROM po_hdr phdr \r\n" + 
	    			"     JOIN indent_hdr ih ON ih.INDENT_ID = phdr.INDENT_ID \r\n" + 
	    			"     WHERE ih.PROJECT_ID = hdr.PM_HDR_ID \r\n" + 
	    			"       AND ih.INDENT_TYPE_CODE IN ('IT001', 'IT002','IT003') \r\n" + 
	    			"       AND phdr.IS_LATEST = 1) AS TOTAL_PO_VALUE,\r\n" + 
	    			"\r\n" + 
	    			"    -- ACTUAL_SPENT = inventory transfer + cash voucher + timesheet\r\n" + 
	    			"    COALESCE((\r\n" + 
	    			"        SELECT SUM(OVER_ALL_COST)\r\n" + 
	    			"        FROM inventroy_material_transfer imt\r\n" + 
	    			"        WHERE imt.TO_PM_HDR_ID = hdr.PM_HDR_ID \r\n" + 
	    			"          AND imt.FROM_PM_HDR_ID != imt.TO_PM_HDR_ID\r\n" + 
	    			"    ), 0) +\r\n" + 
	    			"    COALESCE((\r\n" + 
	    			"        SELECT SUM(CASE WHEN SCM_BUDGET_ALLOCATED > 0 THEN SCM_BUDGET_ALLOCATED ELSE 0 END)\r\n" + 
	    			"        FROM indent_hdr ihdr\r\n" + 
	    			"        WHERE ihdr.PROJECT_ID = hdr.PM_HDR_ID\r\n" + 
	    			"    ), 0) +\r\n" + 
	    			"    COALESCE((\r\n" + 
	    			"        SELECT SUM(TIMESHEET_COST)\r\n" + 
	    			"        FROM timesheet_hdr th\r\n" + 
	    			"        JOIN timesheet_dtl td ON td.T_HDR_ID = th.T_HDR_ID\r\n" + 
	    			"        WHERE th.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
	    			"    ), 0) AS ACTUAL_SPENT,\r\n" + 
	    			"     COALESCE((\r\n" + 
	    			"        SELECT SUM(TIMESHEET_COST)\r\n" + 
	    			"        FROM timesheet_hdr th\r\n" + 
	    			"        JOIN timesheet_dtl td ON td.T_HDR_ID = th.T_HDR_ID\r\n" + 
	    			"        WHERE th.PM_HDR_ID = hdr.PM_HDR_ID\r\n" + 
	    			"    ), 0) AS TIMESHEET_COST,\r\n" + 
	    			"    COALESCE((\r\n" + 
	    			"        SELECT SUM(OVER_ALL_COST)\r\n" + 
	    			"        FROM inventroy_material_transfer imt\r\n" + 
	    			"        WHERE imt.TO_PM_HDR_ID = hdr.PM_HDR_ID \r\n" + 
	    			"          AND imt.FROM_PM_HDR_ID != imt.TO_PM_HDR_ID\r\n" + 
	    			"    ), 0) AS OVER_ALL_COST,\r\n" + 
	    			"    \r\n" + 
	    			"        -- Actual Value\r\n" + 
	    			"    (SELECT SUM(SCM_BUDGET_ALLOCATED) \r\n" + 
	    			"     FROM indent_hdr \r\n" + 
	    			"     WHERE PROJECT_ID = hdr.PM_HDR_ID) AS SCM_BUDGET_ALLOCATED, SUM(DN_VALUE) as DN_VALUE \r\n" +
	    			"FROM \r\n" + 
	    			"    project_hdr hdr\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    customer_mst mst ON mst.CUST_NAME = hdr.CUSTOMER_NAME\r\n" + 
	    			"        INNER JOIN\r\n" + 
	    			"    sales_budget_sheet_hdr sbHdr ON sbHdr.MASTER_ID = hdr.ENQUIRY_ID\r\n" +
	    			"        INNER JOIN \r\n" + 
	    			"	document_status_type_code dtc ON dtc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS \r\n" +
	    		    "        LEFT JOIN\r\n" +
	    			"    debit_note note ON note.PM_HDR_ID = hdr.PM_HDR_ID\r\n"  +
	    			"WHERE hdr.TENANT_ID = '"+tenantId+"' \r\n" + 
	    			"		"+pmHdr+"\r\n" + 
	    			"       "+stage+"\r\n" + 
	    			"       "+cust+"\r\n" + 
	    			"       "+date+"\r\n" +
	    			"GROUP BY hdr.PM_HDR_ID order by hdr.PROJECT_CODE";
	    	
	    	list = this.jdbcTemplate.query(Qry, new GetOverAllProjSpentDrillDownRowMapper());
	    }catch(Exception ex) {
	    	logger.error("getProjConsumedValue DAO Error" + ex);	
	    }
		return list;
	}
}
