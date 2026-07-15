package com.vmfg.mis.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.mis.dao.interfaces.ISalesMisDAO;
import com.vmfg.mis.entity.EmployeeEntity;
import com.vmfg.mis.entity.GetCustomerOrderDtlEntity;
import com.vmfg.mis.entity.GetSalesContDtlEntity;
import com.vmfg.mis.entity.GetSalesConvRatioEntity;
import com.vmfg.mis.entity.GetSalesStageDtlEntity;
import com.vmfg.mis.entity.SalesOrderDetailsList;
import com.vmfg.mis.rowmapper.EmployeeRowMapper;
import com.vmfg.mis.rowmapper.GetCustomerOrderDtlRowMapper;
import com.vmfg.mis.rowmapper.GetSalesContDtlRowMapper;
import com.vmfg.mis.rowmapper.GetSalesConvRatioRowMapper;
import com.vmfg.mis.rowmapper.GetSalesStageDtlRowMapper;
import com.vmfg.mis.rowmapper.SalesOrderDetailsListRowMapper;
import com.vmfg.scm.entity.ProjectHdrDtlEntity;
import com.vmfg.scm.rowmapper.ProjectHdrDtlRowMapper;
import com.vmfg.util.GetPropertyValue;

@Transactional
@Repository
public class SalesMisDAO implements ISalesMisDAO{

	private static final Logger logger = LoggerFactory.getLogger(SalesMisDAO.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	@Override
	public List<SalesOrderDetailsList> leadList(String startDate, String endDate, String tenantId,String pmId,String empId) {
		List<SalesOrderDetailsList> leadList = null;

		try {

//			String getQ = "select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,count(*) AS SE_COUNT from sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ? and hdr.TRANSACTION_STAGE='STG001' and hdr.TRANSACTION_STATUS='DS001' and hdr.TRANSACTION_STATUS_SEQ = 1 and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE)";
	//	String getQ ="select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR ,sum(TENTATIVE_PO_VALUE) AS VAL,count(*) AS SE_COUNT from sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ? and TRANSACTION_STAGE='STG001' and TRANSACTION_STATUS='DS001' and TRANSACTION_STATUS_SEQ = 1 and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE)";
//			leadList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId,empId);
			String getQ;
	        
			if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
	            getQ ="select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,count(*) AS SE_COUNT from sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ? and hdr.TRANSACTION_STAGE='STG001' and hdr.TRANSACTION_STATUS='DS001' and hdr.TRANSACTION_STATUS_SEQ = 1 and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 "
//	            		+ "and pat.ASSIGNED_EMP_ID = ? "
	            		+ "group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE)";
	            leadList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(), startDate, endDate, tenantId, pmId);
	        } else {
	            getQ = "select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,count(*) AS SE_COUNT from sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ? and hdr.TRANSACTION_STAGE='STG001' and hdr.TRANSACTION_STATUS='DS001' and hdr.TRANSACTION_STATUS_SEQ = 1 and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE)";
	            leadList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(), startDate, endDate, tenantId, pmId, empId);
	        }
		} catch (Exception ex) {
			logger.error("leadList error " + ex.getMessage());
		}
		return leadList;
	}
	@Override
	public List<SalesOrderDetailsList> enqList(String startDate, String endDate, String tenantId,String pmId,String empId) {
		List<SalesOrderDetailsList> enqList = null;

		try {

//			String getQ = "select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,count(*) AS SE_COUNT from sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID  LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ? and hdr.TRANSACTION_STAGE !='STG001' and  hdr.TRANSACTION_STATUS_SEQ != 1 and hdr.TENANT_ID = ? AND pat.PM_ID =? and hdr.IS_COMPLETED =0 and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE)";
//			enqList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId,empId);
			
			String getQ;
	        
			if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
	            getQ = "select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,count(*) AS SE_COUNT from sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID  LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ? and hdr.TRANSACTION_STAGE !='STG001' and  hdr.TRANSACTION_STATUS_SEQ != 1 and hdr.TENANT_ID = ? AND pat.PM_ID =? and hdr.IS_COMPLETED =0 and pat.IS_ACTIVE =1 "
//	            		+ "and pat.ASSIGNED_EMP_ID = ? "
	            		+ "group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE)";
	            enqList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId);
	        } else {
	            getQ ="select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,count(*) AS SE_COUNT from sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID  LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ? and hdr.TRANSACTION_STAGE !='STG001' and  hdr.TRANSACTION_STATUS_SEQ != 1 and hdr.TENANT_ID = ? AND pat.PM_ID =? and hdr.IS_COMPLETED =0 and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE)";
	            enqList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId,empId);
	        }

		} catch (Exception ex) {
			logger.error("enqList error " + ex.getMessage());
		}
		return enqList;
	}

	@Override
	public List<SalesOrderDetailsList> wonList(String startDate, String endDate, String tenantId,String pmId,String empId) {
		List<SalesOrderDetailsList> wonList = null;

		try {

		//	String getQ = "select SUBSTRING(COMPLETED_DATETIME ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)   end VAL,count(*) AS SE_COUNT from sales_enq_hdr  hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID where DATE(COMPLETED_DATETIME) between ? and ? and IS_COMPLETED =1 and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? group by month(COMPLETED_DATETIME) , year(COMPLETED_DATETIME)";
		//	wonList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId,empId);

			
			String getFilePath=GetPropertyValue.getPropValue("SALES_MIS_STATUS_LIST", tenantId, jdbcTemplate);
			String splitarr[]=  getFilePath.split(",");
			String status ="";
			for(int i =0;i<splitarr.length;i++) {
				if(status.equalsIgnoreCase("")) {
					status = splitarr[i];
				}else {
					status = status +"','"+splitarr[i];
				}
			}
			String getQ;
//			String getQ ="SELECT\n"
//					+ "SUBSTRING(UPDATED_ON ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)   end VAL,count(*) AS SE_COUNT\n"
//			//		+ "SUBSTRING(UPDATED_ON ,1 ,7)AS MONTH_YEAR ,  SUM(hdr.TENTATIVE_PO_VALUE) AS VAL,count(*) AS SE_COUNT\n"
//					+ "\n"
//					+ "FROM\n"
//					+ "    sales_enq_hdr hdr\n"
//					+ "        INNER JOIN\n"
//					+ "    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
//					+ "        LEFT JOIN\n"
//					+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\n"
//					+ "        LEFT JOIN\n"
//					+ "    stg_master sm ON sm.STG_CODE = hdr.TRANSACTION_STAGE\n"
//					+ "        LEFT JOIN\n"
//					+ "    document_status_type_code dstc1 ON dstc1.DOCUMENT_STATUS_TYPE_CODE = dtl.TRANSACTION_STATUS\n"
//					+ "        LEFT JOIN\n"
//					+ "    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
//					+ "        LEFT JOIN\n"
//					+ "    sales_budget_sheet_hdr sbsh ON sbsh.MASTER_ID = hdr.SE_ID\n"
//					+ "        INNER JOIN\n"
//					+ "    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\n"
//					+ "WHERE\n"
//					+ "    hdr.CREATED_DATETIME BETWEEN ? AND ? \n"
//					+ "        AND hdr.TENANT_ID = ? \n"
//					+ "        AND pat.TENANT_ID = hdr.TENANT_ID\n"
//					+ "        AND pat.ASSIGNED_EMP_ID = ? \n"
//					+ "        AND pat.IS_ACTIVE = 1\n"
//					+ "        AND pat.PM_ID = ? \n"
//					+ "        AND ssd.SS_ID = (SELECT \n"
//					+ "            SS_ID\n"
//					+ "        FROM\n"
//					+ "            sales_status_dtl\n"
//					+ "        WHERE\n"
//					+ "            SEQUENCE_STATUS = 'DS003'\n"
//					+ "                AND REFERENCE_ID = dtl.SE_DTL_ID\n"
//					+ "        ORDER BY UPDATED_ON DESC\n"
//					+ "        LIMIT 1)\n"
//					+ "        AND hdr.TRANSACTION_STATUS IN ('"+status+"')\n"
//					+ "        AND ssd.SEQUENCE_STATUS = 'DS003'\n"
//					+ "GROUP BY MONTH(ssd.UPDATED_ON) , YEAR(ssd.UPDATED_ON)";
//			wonList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,empId,pmId);
			if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)){
	            getQ = "SELECT\n"
						+ "SUBSTRING(UPDATED_ON ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)   end VAL,count(*) AS SE_COUNT\n"
						//		+ "SUBSTRING(UPDATED_ON ,1 ,7)AS MONTH_YEAR ,  SUM(hdr.TENTATIVE_PO_VALUE) AS VAL,count(*) AS SE_COUNT\n"
								+ "\n"
								+ "FROM\n"
								+ "    sales_enq_hdr hdr\n"
								+ "        INNER JOIN\n"
								+ "    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
								+ "        LEFT JOIN\n"
								+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\n"
								+ "        LEFT JOIN\n"
								+ "    stg_master sm ON sm.STG_CODE = hdr.TRANSACTION_STAGE\n"
								+ "        LEFT JOIN\n"
								+ "    document_status_type_code dstc1 ON dstc1.DOCUMENT_STATUS_TYPE_CODE = dtl.TRANSACTION_STATUS\n"
								+ "        LEFT JOIN\n"
								+ "    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
								+ "        LEFT JOIN\n"
								+ "    sales_budget_sheet_hdr sbsh ON sbsh.MASTER_ID = hdr.SE_ID\n"
								+ "        INNER JOIN\n"
								+ "    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\n"
								+ "WHERE\n"
								+ "    hdr.CREATED_DATETIME BETWEEN ? AND ? \n"
								+ "        AND hdr.TENANT_ID = ? \n"
								+ "        AND pat.TENANT_ID = hdr.TENANT_ID\n"
//								+ "        AND pat.ASSIGNED_EMP_ID = ? \n"
								+ "        AND pat.IS_ACTIVE = 1\n"
								+ "        AND pat.PM_ID = ? \n"
								+ "        AND ssd.SS_ID = (SELECT \n"
								+ "            SS_ID\n"
								+ "        FROM\n"
								+ "            sales_status_dtl\n"
								+ "        WHERE\n"
								+ "            SEQUENCE_STATUS = 'DS003'\n"
								+ "                AND REFERENCE_ID = dtl.SE_DTL_ID\n"
								+ "        ORDER BY UPDATED_ON DESC\n"
								+ "        LIMIT 1)\n"
								+ "        AND hdr.TRANSACTION_STATUS IN ('"+status+"')\n"
								+ "        AND ssd.SEQUENCE_STATUS = 'DS003'\n"
								+ "GROUP BY MONTH(ssd.UPDATED_ON) , YEAR(ssd.UPDATED_ON)";
						wonList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId);
	        } else {
	            getQ ="SELECT\n"
						+ "SUBSTRING(UPDATED_ON ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)   end VAL,count(*) AS SE_COUNT\n"
						//		+ "SUBSTRING(UPDATED_ON ,1 ,7)AS MONTH_YEAR ,  SUM(hdr.TENTATIVE_PO_VALUE) AS VAL,count(*) AS SE_COUNT\n"
								+ "\n"
								+ "FROM\n"
								+ "    sales_enq_hdr hdr\n"
								+ "        INNER JOIN\n"
								+ "    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
								+ "        LEFT JOIN\n"
								+ "    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS\n"
								+ "        LEFT JOIN\n"
								+ "    stg_master sm ON sm.STG_CODE = hdr.TRANSACTION_STAGE\n"
								+ "        LEFT JOIN\n"
								+ "    document_status_type_code dstc1 ON dstc1.DOCUMENT_STATUS_TYPE_CODE = dtl.TRANSACTION_STATUS\n"
								+ "        LEFT JOIN\n"
								+ "    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
								+ "        LEFT JOIN\n"
								+ "    sales_budget_sheet_hdr sbsh ON sbsh.MASTER_ID = hdr.SE_ID\n"
								+ "        INNER JOIN\n"
								+ "    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\n"
								+ "WHERE\n"
								+ "    hdr.CREATED_DATETIME BETWEEN ? AND ? \n"
								+ "        AND hdr.TENANT_ID = ? \n"
								+ "        AND pat.TENANT_ID = hdr.TENANT_ID\n"
								+ "        AND pat.ASSIGNED_EMP_ID = ? \n"
								+ "        AND pat.IS_ACTIVE = 1\n"
								+ "        AND pat.PM_ID = ? \n"
								+ "        AND ssd.SS_ID = (SELECT \n"
								+ "            SS_ID\n"
								+ "        FROM\n"
								+ "            sales_status_dtl\n"
								+ "        WHERE\n"
								+ "            SEQUENCE_STATUS = 'DS003'\n"
								+ "                AND REFERENCE_ID = dtl.SE_DTL_ID\n"
								+ "        ORDER BY UPDATED_ON DESC\n"
								+ "        LIMIT 1)\n"
								+ "        AND hdr.TRANSACTION_STATUS IN ('"+status+"')\n"
								+ "        AND ssd.SEQUENCE_STATUS = 'DS003'\n"
								+ "GROUP BY MONTH(ssd.UPDATED_ON) , YEAR(ssd.UPDATED_ON)";
	            wonList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,empId,pmId);
	        }
		} catch (Exception ex) {
			logger.error("wonList error " + ex.getMessage());
		}
		return wonList;
	}

	@Override
	public List<SalesOrderDetailsList> lostList(String startDate, String endDate, String tenantId,String pmId,String empId) {
		List<SalesOrderDetailsList> lostList = null;

		try {
            String getQ;
//			String getQ = "select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR , case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,count(*) AS SE_COUNT from sales_enq_hdr  hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ?  and hdr.TRANSACTION_STATUS='DS004' and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE);";
//			getQ =" SELECT \n"
//					+ "    SUBSTRING(ssd.UPDATED_ON, 1, 7) AS MONTH_YEAR,\n"
//					+ "     case when SUM(sbsh.TOTAL_BUDGET_COST) is null or 0 then SUM(hdr.TENTATIVE_PO_VALUE) else SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,\n"
//					+ "    COUNT(*) AS SE_COUNT\n"
//					+ "FROM\n"
//					+ "    sales_enq_hdr hdr\n"
//					+ "        INNER JOIN\n"
//					+ "	sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
//					+ "		INNER JOIN\n"
//					+ "    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
//					+ "        INNER JOIN\n"
//					+ "    sales_status_dtl ssd ON ssd.SEQUENCE_STATUS = hdr.TRANSACTION_STATUS LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID \n"
//					+ "WHERE\n"
//					+ "    DATE(ssd.UPDATED_ON) BETWEEN ? AND ? \n"
//					+ "     AND    hdr.TRANSACTION_STATUS = 'DS004' and ssd.REFERENCE_ID = dtl.SE_DTL_ID \n"
//					+ "        AND hdr.TENANT_ID = ? \n"
//					+ "        AND pat.PM_ID = ? \n"
//					+ "        AND pat.IS_ACTIVE = 1\n"
//					+ "        AND pat.ASSIGNED_EMP_ID = ? \n"
//					+ "GROUP BY MONTH(ssd.UPDATED_ON) , YEAR(ssd.UPDATED_ON)";
//			lostList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId,empId);
            if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
                getQ = "SELECT \n"
                        + "    SUBSTRING(ssd.UPDATED_ON, 1, 7) AS MONTH_YEAR,\n"
                        + "    CASE \n"
                        + "        WHEN SUM(sbsh.TOTAL_BUDGET_COST) IS NULL OR SUM(sbsh.TOTAL_BUDGET_COST) = 0 \n"
                        + "        THEN SUM(hdr.TENTATIVE_PO_VALUE) \n"
                        + "        ELSE SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST) \n"
                        + "    END AS VAL,\n"
                        + "    COUNT(*) AS SE_COUNT\n"
                        + "FROM sales_enq_hdr hdr\n"
                        + "    INNER JOIN sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
                        + "    INNER JOIN process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
                        + "    INNER JOIN sales_status_dtl ssd ON ssd.SEQUENCE_STATUS = hdr.TRANSACTION_STATUS \n"
                        + "    LEFT JOIN sales_budget_sheet_hdr sbsh ON sbsh.MASTER_ID = hdr.SE_ID\n"
                        + "WHERE DATE(ssd.UPDATED_ON) BETWEEN ? AND ? \n"
                        + "    AND hdr.TRANSACTION_STATUS = 'DS004'\n"
                        + "    AND ssd.REFERENCE_ID = dtl.SE_DTL_ID\n"
                        + "    AND hdr.TENANT_ID = ?\n"
                        + "    AND pat.PM_ID = ?\n"
                        + "    AND pat.IS_ACTIVE = 1\n"
//                        + "    AND pat.ASSIGNED_EMP_ID = ?\n"
                        + "GROUP BY YEAR(ssd.UPDATED_ON), MONTH(ssd.UPDATED_ON)";

                lostList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(), startDate, endDate, tenantId, pmId);
            } else {
                getQ = "SELECT \n"
                        + "    SUBSTRING(ssd.UPDATED_ON, 1, 7) AS MONTH_YEAR,\n"
                        + "    CASE \n"
                        + "        WHEN SUM(sbsh.TOTAL_BUDGET_COST) IS NULL OR SUM(sbsh.TOTAL_BUDGET_COST) = 0 \n"
                        + "        THEN SUM(hdr.TENTATIVE_PO_VALUE) \n"
                        + "        ELSE SUM(sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST) \n"
                        + "    END AS VAL,\n"
                        + "    COUNT(*) AS SE_COUNT\n"
                        + "FROM sales_enq_hdr hdr\n"
                        + "    INNER JOIN sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
                        + "    INNER JOIN process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
                        + "    INNER JOIN sales_status_dtl ssd ON ssd.SEQUENCE_STATUS = hdr.TRANSACTION_STATUS \n"
                        + "    LEFT JOIN sales_budget_sheet_hdr sbsh ON sbsh.MASTER_ID = hdr.SE_ID\n"
                        + "WHERE DATE(ssd.UPDATED_ON) BETWEEN ? AND ? \n"
                        + "    AND hdr.TRANSACTION_STATUS = 'DS004'\n"
                        + "    AND ssd.REFERENCE_ID = dtl.SE_DTL_ID\n"
                        + "    AND hdr.TENANT_ID = ?\n"
                        + "    AND pat.PM_ID = ?\n"
                        + "    AND pat.IS_ACTIVE = 1\n"
                        + "    AND pat.ASSIGNED_EMP_ID = ?\n"
                        + "GROUP BY YEAR(ssd.UPDATED_ON), MONTH(ssd.UPDATED_ON)";
                lostList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(), startDate, endDate, tenantId, pmId, empId);
            }
		} catch (Exception ex) {
			logger.error("lostList error " + ex.getMessage());
		}
		return lostList;
	}

	@Override
	public List<SalesOrderDetailsList> holdList(String startDate, String endDate, String tenantId,String pmId,String empId) {
		List<SalesOrderDetailsList> holdList = null;

		try {
			String getQ;
//			String getQ = "select SUBSTRING(ENQUIRY_DATE ,1 ,7)AS MONTH_YEAR ,sum(TENTATIVE_PO_VALUE) AS VAL,count(*) AS SE_COUNT from sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID where ENQUIRY_DATE between ? and ? and hdr.TRANSACTION_STATUS='DS005' and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? group by month(ENQUIRY_DATE) , year(ENQUIRY_DATE)";
//			
//			getQ =" SELECT \n"
//					+ "    SUBSTRING(ssd.UPDATED_ON, 1, 7) AS MONTH_YEAR,\n"
//					+ "    SUM(hdr.TENTATIVE_PO_VALUE) AS VAL,\n"
//					+ "    COUNT(*) AS SE_COUNT\n"
//					+ "FROM\n"
//					+ "    sales_enq_hdr hdr\n"
//					+ "        INNER JOIN\n"
//					+ "	sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
//					+ "		INNER JOIN\n"
//					+ "    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
//					+ "        INNER JOIN\n"
//					+ "    sales_status_dtl ssd ON ssd.SEQUENCE_STATUS = hdr.TRANSACTION_STATUS LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID \n"
//					+ "WHERE\n"
//					+ "    DATE(ssd.UPDATED_ON) BETWEEN ? AND ? \n"
//					+ "     AND    hdr.TRANSACTION_STATUS = 'DS005' and ssd.REFERENCE_ID = dtl.SE_DTL_ID \n"
//					+ "        AND hdr.TENANT_ID = ? \n"
//					+ "        AND pat.PM_ID = ? \n"
//					+ "        AND pat.IS_ACTIVE = 1\n"
//					+ "        AND pat.ASSIGNED_EMP_ID = ? \n"
//					+ "GROUP BY MONTH(ssd.UPDATED_ON) , YEAR(ssd.UPDATED_ON)";
//			holdList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId,empId);
			if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
                getQ = "SELECT \n"
                        + "    SUBSTRING(ssd.UPDATED_ON, 1, 7) AS MONTH_YEAR,\n"
                        + "    SUM(hdr.TENTATIVE_PO_VALUE) AS VAL,\n"
                        + "    COUNT(*) AS SE_COUNT\n"
                        + "FROM\n"
                        + "    sales_enq_hdr hdr\n"
                        + "        INNER JOIN\n"
                        + "    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
                        + "        INNER JOIN\n"
                        + "    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
                        + "        INNER JOIN\n"
                        + "    sales_status_dtl ssd ON ssd.SEQUENCE_STATUS = hdr.TRANSACTION_STATUS\n"
                        + "        LEFT JOIN\n"
                        + "    sales_budget_sheet_hdr sbsh ON sbsh.MASTER_ID = hdr.SE_ID\n"
                        + "WHERE\n"
                        + "    DATE(ssd.UPDATED_ON) BETWEEN ? AND ?\n"
                        + "    AND hdr.TRANSACTION_STATUS = 'DS005'\n"
                        + "    AND ssd.REFERENCE_ID = dtl.SE_DTL_ID\n"
                        + "    AND hdr.TENANT_ID = ?\n"
                        + "    AND pat.PM_ID = ?\n"
                        + "    AND pat.IS_ACTIVE = 1\n"
//                        + "    AND pat.ASSIGNED_EMP_ID = ?\n"
                        + "GROUP BY MONTH(ssd.UPDATED_ON), YEAR(ssd.UPDATED_ON)";

                holdList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId);
            } else {
                getQ = "SELECT \n"
                        + "    SUBSTRING(ssd.UPDATED_ON, 1, 7) AS MONTH_YEAR,\n"
                        + "    SUM(hdr.TENTATIVE_PO_VALUE) AS VAL,\n"
                        + "    COUNT(*) AS SE_COUNT\n"
                        + "FROM\n"
                        + "    sales_enq_hdr hdr\n"
                        + "        INNER JOIN\n"
                        + "    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\n"
                        + "        INNER JOIN\n"
                        + "    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\n"
                        + "        INNER JOIN\n"
                        + "    sales_status_dtl ssd ON ssd.SEQUENCE_STATUS = hdr.TRANSACTION_STATUS\n"
                        + "        LEFT JOIN\n"
                        + "    sales_budget_sheet_hdr sbsh ON sbsh.MASTER_ID = hdr.SE_ID\n"
                        + "WHERE\n"
                        + "    DATE(ssd.UPDATED_ON) BETWEEN ? AND ?\n"
                        + "    AND hdr.TRANSACTION_STATUS = 'DS005'\n"
                        + "    AND ssd.REFERENCE_ID = dtl.SE_DTL_ID\n"
                        + "    AND hdr.TENANT_ID = ?\n"
                        + "    AND pat.PM_ID = ?\n"
                        + "    AND pat.IS_ACTIVE = 1\n"
                        + "    AND pat.ASSIGNED_EMP_ID = ?\n"
                        + "GROUP BY MONTH(ssd.UPDATED_ON), YEAR(ssd.UPDATED_ON)";
                holdList = this.jdbcTemplate.query(getQ, new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,pmId,empId);
            }

		} catch (Exception ex) {
			logger.error("holdList error " + ex.getMessage());
		}
		return holdList;
	}

	@Override
	public List<GetSalesStageDtlEntity> getSalesStageDtl(String startDate, String endDate, String tenantId,String pmId,String empId) {
		List<GetSalesStageDtlEntity> salesOrderDetailsList = null;

		try {
            String getQ;
//			String getQ = "SELECT \n"
//					+ "  dtm.DOCUMENT_STATUS_TYPE_DESCRIPTION AS DESCRIPTION ,sum(TENTATIVE_PO_VALUE) AS VAL,count(*) AS SE_COUNT\n"
//					+ "FROM\n"
//					+ "    sales_enq_hdr hdr inner join \n"
//					+ "    document_status_type_code dtm on hdr.TRANSACTION_STATUS = dtm.DOCUMENT_STATUS_TYPE_CODE inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
//					+ "WHERE\n"
//					+ "    ENQUIRY_DATE BETWEEN ? AND ? and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? \n"
//					+ "GROUP BY TRANSACTION_STATUS , TRANSACTION_STATUS_SEQ ";
//			salesOrderDetailsList = this.jdbcTemplate.query(getQ, new GetSalesStageDtlRowMapper(),startDate,endDate, tenantId,pmId,empId);
            if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)){
                getQ = "SELECT \n"
    					+ "  dtm.DOCUMENT_STATUS_TYPE_DESCRIPTION AS DESCRIPTION ,sum(TENTATIVE_PO_VALUE) AS VAL,count(*) AS SE_COUNT\n"
    					+ "FROM\n"
    					+ "    sales_enq_hdr hdr inner join \n"
    					+ "    document_status_type_code dtm on hdr.TRANSACTION_STATUS = dtm.DOCUMENT_STATUS_TYPE_CODE inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
    					+ "WHERE\n"
    					+ "    ENQUIRY_DATE BETWEEN ? AND ? and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 "
//    					+ "and pat.ASSIGNED_EMP_ID = ? \n"
    					+ "GROUP BY TRANSACTION_STATUS , TRANSACTION_STATUS_SEQ ";
    			salesOrderDetailsList = this.jdbcTemplate.query(getQ, new GetSalesStageDtlRowMapper(),startDate,endDate, tenantId,pmId);
            } else {
                getQ = "SELECT \n"
    					+ "  dtm.DOCUMENT_STATUS_TYPE_DESCRIPTION AS DESCRIPTION ,sum(TENTATIVE_PO_VALUE) AS VAL,count(*) AS SE_COUNT\n"
    					+ "FROM\n"
    					+ "    sales_enq_hdr hdr inner join \n"
    					+ "    document_status_type_code dtm on hdr.TRANSACTION_STATUS = dtm.DOCUMENT_STATUS_TYPE_CODE inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
    					+ "WHERE\n"
    					+ "    ENQUIRY_DATE BETWEEN ? AND ? and hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? \n"
    					+ "GROUP BY TRANSACTION_STATUS , TRANSACTION_STATUS_SEQ ";
    			salesOrderDetailsList = this.jdbcTemplate.query(getQ, new GetSalesStageDtlRowMapper(),startDate,endDate, tenantId,pmId,empId);
            }

		} catch (Exception ex) {
			logger.error("getSalesStageDtl error " + ex.getMessage());
		}
		return salesOrderDetailsList;
	}

	@Override
	public List<GetCustomerOrderDtlEntity> getCustomerOrderDtl(String startDate, String endDate, String tenantId,String pmId,String empId) {
		List<GetCustomerOrderDtlEntity> getCustomerOrderDtlList = null;

		try {
            String getQ;
//			String getQ = "SELECT \n"
//					+ "    COUNT(*) AS NO_OF_ORDERS, CUSTOMER_NAME, hdr.TENANT_ID\n"
//					+ "FROM\n"
//					+ "    sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
//					+ "WHERE\n"
//					+ "    DATE(CREATED_DATETIME) BETWEEN ? AND ? \n"
//					+ "        AND hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? and IS_COMPLETED=1 \n"
//					+ "GROUP BY CUSTOMER_NAME order by COUNT(*) desc limit 10 ";
//			getCustomerOrderDtlList = this.jdbcTemplate.query(getQ, new GetCustomerOrderDtlRowMapper(),startDate,endDate, tenantId,pmId,empId);
            if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
                getQ = "SELECT \n"
    					+ "    COUNT(*) AS NO_OF_ORDERS, CUSTOMER_NAME, hdr.TENANT_ID\n"
    					+ "FROM\n"
    					+ "    sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
    					+ "WHERE\n"
    					+ "    DATE(CREATED_DATETIME) BETWEEN ? AND ? \n"
    					+ "        AND hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 "
//    					+ "and pat.ASSIGNED_EMP_ID = ? and IS_COMPLETED=1 \n"
    					+ "GROUP BY CUSTOMER_NAME order by COUNT(*) desc limit 10 ";
    			getCustomerOrderDtlList = this.jdbcTemplate.query(getQ, new GetCustomerOrderDtlRowMapper(),startDate,endDate, tenantId,pmId);
            } else {
                getQ = "SELECT \n"
    					+ "    COUNT(*) AS NO_OF_ORDERS, CUSTOMER_NAME, hdr.TENANT_ID\n"
    					+ "FROM\n"
    					+ "    sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
    					+ "WHERE\n"
    					+ "    DATE(CREATED_DATETIME) BETWEEN ? AND ? \n"
    					+ "        AND hdr.TENANT_ID = ? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? and IS_COMPLETED=1 \n"
    					+ "GROUP BY CUSTOMER_NAME order by COUNT(*) desc limit 10 ";
    			getCustomerOrderDtlList = this.jdbcTemplate.query(getQ, new GetCustomerOrderDtlRowMapper(),startDate,endDate, tenantId,pmId,empId);
            }

		} catch (Exception ex) {
			logger.error("getCustomerOrderDtl error " + ex.getMessage());
		}
		return getCustomerOrderDtlList;
	}

	@Override
	public List<GetSalesConvRatioEntity> getSalesConvRatio(String startDate, String endDate, String tenantId,String pmId,String empId) {
		List<GetSalesConvRatioEntity> getCustomerOrderDtlList = null;

		try {
            String getQ;
//			String getQ = "SELECT \n"
//					+ "    CUSTOMER_NAME,\n"
//					+ "    PROJECT_NAME,\n"
//					+ "    CREATED_DATETIME,\n"
//					+ "    DATE(CREATED_DATETIME) AS CREATED_DATE,\n"
//					+ "    COMPLETED_DATETIME,\n"
//					+ "    DATE(COMPLETED_DATETIME) AS COMPLETED_DATE,\n"
//					+ "    DATEDIFF(COMPLETED_DATETIME, CREATED_DATETIME) AS DATE_DIFF,\n"
//					+ "    hdr.TENANT_ID,PROJECT_HANDOVER_DATE,ENQUIRY_CODE\n"
//					+ "FROM\n"
//					+ "    sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
//					+ "WHERE\n"
//					+ "    DATE(CREATED_DATETIME) BETWEEN ? AND ? and hdr.TENANT_ID =? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? and hdr.IS_COMPLETED =1 ";
//			getCustomerOrderDtlList = this.jdbcTemplate.query(getQ, new GetSalesConvRatioRowMapper(),startDate,endDate, tenantId,pmId,empId);
            if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
                getQ = "SELECT \n"
    					+ "    CUSTOMER_NAME,\n"
    					+ "    PROJECT_NAME,\n"
    					+ "    CREATED_DATETIME,\n"
    					+ "    DATE(CREATED_DATETIME) AS CREATED_DATE,\n"
    					+ "    COMPLETED_DATETIME,\n"
    					+ "    DATE(COMPLETED_DATETIME) AS COMPLETED_DATE,\n"
    					+ "    DATEDIFF(COMPLETED_DATETIME, CREATED_DATETIME) AS DATE_DIFF,\n"
    					+ "    hdr.TENANT_ID,PROJECT_HANDOVER_DATE,ENQUIRY_CODE\n"
    					+ "FROM\n"
    					+ "    sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
    					+ "WHERE\n"
    					+ "    DATE(CREATED_DATETIME) BETWEEN ? AND ? and hdr.TENANT_ID =? AND pat.PM_ID =? and pat.IS_ACTIVE =1 "
//    					+ "and pat.ASSIGNED_EMP_ID = ? "
    					+ "and hdr.IS_COMPLETED =1 ";
    			getCustomerOrderDtlList = this.jdbcTemplate.query(getQ, new GetSalesConvRatioRowMapper(),startDate,endDate, tenantId,pmId);
            } else {
                getQ = "SELECT \n"
    					+ "    CUSTOMER_NAME,\n"
    					+ "    PROJECT_NAME,\n"
    					+ "    CREATED_DATETIME,\n"
    					+ "    DATE(CREATED_DATETIME) AS CREATED_DATE,\n"
    					+ "    COMPLETED_DATETIME,\n"
    					+ "    DATE(COMPLETED_DATETIME) AS COMPLETED_DATE,\n"
    					+ "    DATEDIFF(COMPLETED_DATETIME, CREATED_DATETIME) AS DATE_DIFF,\n"
    					+ "    hdr.TENANT_ID,PROJECT_HANDOVER_DATE,ENQUIRY_CODE\n"
    					+ "FROM\n"
    					+ "    sales_enq_hdr hdr inner join process_assigned_team pat on pat.MASTER_ID = hdr.SE_ID \n"
    					+ "WHERE\n"
    					+ "    DATE(CREATED_DATETIME) BETWEEN ? AND ? and hdr.TENANT_ID =? AND pat.PM_ID =? and pat.IS_ACTIVE =1 and pat.ASSIGNED_EMP_ID = ? and hdr.IS_COMPLETED =1 ";
    			getCustomerOrderDtlList = this.jdbcTemplate.query(getQ, new GetSalesConvRatioRowMapper(),startDate,endDate, tenantId,pmId,empId);
            }

		} catch (Exception ex) {
			logger.error("getCustomerOrderDtl error " + ex.getMessage());
		}
		return getCustomerOrderDtlList;
	}

	@Override
	public List<GetSalesContDtlEntity> getSalesContDtl(String startDate, String endDate, String tenantId,
			String projectId,String pmId,String empId) {
		List<GetSalesContDtlEntity> getSalesContDtl = null;

		try {
			String divieBy ="";
			String uom = "";
			String projId ="";
			if(projectId.equalsIgnoreCase("getAll")) {
				projId = "like '%%'";
				divieBy = "1e7";
				uom ="C";
			}else {
				projId = " = "+projectId+"";
				divieBy = "1e5";
				uom ="L";
			}
			
			String getFilePath=GetPropertyValue.getPropValue("SALES_MIS_STATUS_LIST", tenantId, jdbcTemplate);
			String splitarr[]=  getFilePath.split(",");
			String status ="";
			for(int i =0;i<splitarr.length;i++) {
				if(status.equalsIgnoreCase("")) {
					status = splitarr[i];
				}else {
					status = status +"','"+splitarr[i];
				}
			}
			String getQ;

//			String getQ = "SELECT \n"
//					+ "   ( SUM(TOTAL_BUDGET_COST) + SUM(CR_COST))  /"+divieBy+" AS TOTAL_BUDGET_COST,\n"
//					+ "   ( SUM(shdr.FINAL_SALE_VALUE ) + SUM(shdr.FINAL_CR_SALE_COST )) /"+divieBy+" AS FINAL_SALE,\n"
//					+ "   ( SUM(SALE_PERCENT)+ SUM(CR_SALE_PERCENT)) /"+divieBy+" AS VAL\n"
//					+ "FROM\n"
//					+ "    sales_budget_sheet_hdr shdr\n"
//					+ "        INNER JOIN\n"
//					+ "    sales_enq_hdr hdr ON hdr.SE_ID = shdr.MASTER_ID\n"
//					+ "WHERE\n"
//					+ "    DATE(hdr.CREATED_DATETIME) BETWEEN ? AND ? and hdr.SE_ID "+projId+" and shdr.TENANT_ID = ?  ";
			
//			String getQ="SELECT \r\n" + 
//					"   ( SUM(TOTAL_BUDGET_COST) + SUM(CR_COST))  /"+divieBy+" AS TOTAL_BUDGET_COST,\n" +
//					"   ( SUM(shdr.FINAL_SALE_VALUE ) + SUM(shdr.FINAL_CR_SALE_COST )) /"+divieBy+" AS FINAL_SALE,\n" +
//					"   ( SUM(SALE_PERCENT)+ SUM(CR_SALE_PERCENT)) /"+divieBy+" AS VAL\n" +
//					"FROM\r\n" + 
//					"    sales_enq_hdr hdr\r\n" + 
//					"    INNER JOIN\r\n" + 
//					"    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\r\n" + 
//					"        LEFT JOIN\r\n" + 
//					"    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\r\n" + 
//					"        LEFT JOIN\r\n" + 
//					"    sales_budget_sheet_hdr shdr ON shdr.MASTER_ID = hdr.SE_ID\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
//					"WHERE\r\n" + 
//					"    hdr.CREATED_DATETIME BETWEEN ? AND ?\r\n" + 
//					"        AND hdr.TENANT_ID = ?\r\n" + 
//					"        AND pat.TENANT_ID = hdr.TENANT_ID\r\n" + 
////					"        AND pat.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
//					"        AND pat.ASSIGNED_EMP_ID = ?\r\n" +
//					"        AND pat.IS_ACTIVE = 1 \r\n" + 
//					"        AND hdr.SE_ID "+projId+"\r\n" + 
//					"        AND ssd.SS_ID = (SELECT \r\n" + 
//					"            SS_ID\r\n" + 
//					"        FROM\r\n" + 
//					"            sales_status_dtl\r\n" + 
//					"        WHERE\r\n" + 
//					"            SEQUENCE_STATUS = 'DS003'\r\n" + 
//					"                AND REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
//					"        ORDER BY UPDATED_ON DESC\r\n" + 
//					"        LIMIT 1)\r\n" + 
//					"      AND pat.PM_ID = '"+pmId+"' AND hdr.TRANSACTION_STATUS in( '"+status+"') AND ssd.SEQUENCE_STATUS = 'DS003'";
//			
//			getSalesContDtl = this.jdbcTemplate.query(getQ, new GetSalesContDtlRowMapper(),startDate,endDate, tenantId);
//			getSalesContDtl.get(0).setUom(uom);
			if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
                getQ = "SELECT \r\n" + 
    					"   ( SUM(TOTAL_BUDGET_COST) + SUM(CR_COST))  /"+divieBy+" AS TOTAL_BUDGET_COST,\n" +
    					"   ( SUM(shdr.FINAL_SALE_VALUE ) + SUM(shdr.FINAL_CR_SALE_COST )) /"+divieBy+" AS FINAL_SALE,\n" +
    					"   ( SUM(SALE_PERCENT)+ SUM(CR_SALE_PERCENT)) /"+divieBy+" AS VAL\n" +
    					"FROM\r\n" + 
    					"    sales_enq_hdr hdr\r\n" + 
    					"    INNER JOIN\r\n" + 
    					"    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\r\n" + 
    					"        LEFT JOIN\r\n" + 
    					"    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\r\n" + 
    					"        LEFT JOIN\r\n" + 
    					"    sales_budget_sheet_hdr shdr ON shdr.MASTER_ID = hdr.SE_ID\r\n" + 
    					"        INNER JOIN\r\n" + 
    					"    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
    					"WHERE\r\n" + 
    					"    hdr.CREATED_DATETIME BETWEEN ? AND ?\r\n" + 
    					"        AND hdr.TENANT_ID = ?\r\n" + 
    					"        AND pat.TENANT_ID = hdr.TENANT_ID\r\n" + 
//    					"        AND pat.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
//    					"        AND pat.ASSIGNED_EMP_ID = ?\r\n" +
    					"        AND pat.IS_ACTIVE = 1 \r\n" + 
    					"        AND hdr.SE_ID "+projId+"\r\n" + 
    					"        AND ssd.SS_ID = (SELECT \r\n" + 
    					"            SS_ID\r\n" + 
    					"        FROM\r\n" + 
    					"            sales_status_dtl\r\n" + 
    					"        WHERE\r\n" + 
    					"            SEQUENCE_STATUS = 'DS003'\r\n" + 
    					"                AND REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
    					"        ORDER BY UPDATED_ON DESC\r\n" + 
    					"        LIMIT 1)\r\n" + 
    					"      AND pat.PM_ID = '"+pmId+"' AND hdr.TRANSACTION_STATUS in( '"+status+"') AND ssd.SEQUENCE_STATUS = 'DS003'";
                getSalesContDtl = this.jdbcTemplate.query(getQ, new GetSalesContDtlRowMapper(),startDate,endDate, tenantId);
            } else {
                getQ = "SELECT \r\n" + 
    					"   ( SUM(TOTAL_BUDGET_COST) + SUM(CR_COST))  /"+divieBy+" AS TOTAL_BUDGET_COST,\n" +
    					"   ( SUM(shdr.FINAL_SALE_VALUE ) + SUM(shdr.FINAL_CR_SALE_COST )) /"+divieBy+" AS FINAL_SALE,\n" +
    					"   ( SUM(SALE_PERCENT)+ SUM(CR_SALE_PERCENT)) /"+divieBy+" AS VAL\n" +
    					"FROM\r\n" + 
    					"    sales_enq_hdr hdr\r\n" + 
    					"    INNER JOIN\r\n" + 
    					"    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\r\n" + 
    					"        LEFT JOIN\r\n" + 
    					"    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\r\n" + 
    					"        LEFT JOIN\r\n" + 
    					"    sales_budget_sheet_hdr shdr ON shdr.MASTER_ID = hdr.SE_ID\r\n" + 
    					"        INNER JOIN\r\n" + 
    					"    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
    					"WHERE\r\n" + 
    					"    hdr.CREATED_DATETIME BETWEEN ? AND ?\r\n" + 
    					"        AND hdr.TENANT_ID = ?\r\n" + 
    					"        AND pat.TENANT_ID = hdr.TENANT_ID\r\n" + 
//    					"        AND pat.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
    					"        AND pat.ASSIGNED_EMP_ID = ?\r\n" +
    					"        AND pat.IS_ACTIVE = 1 \r\n" + 
    					"        AND hdr.SE_ID "+projId+"\r\n" + 
    					"        AND ssd.SS_ID = (SELECT \r\n" + 
    					"            SS_ID\r\n" + 
    					"        FROM\r\n" + 
    					"            sales_status_dtl\r\n" + 
    					"        WHERE\r\n" + 
    					"            SEQUENCE_STATUS = 'DS003'\r\n" + 
    					"                AND REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
    					"        ORDER BY UPDATED_ON DESC\r\n" + 
    					"        LIMIT 1)\r\n" + 
    					"      AND pat.PM_ID = '"+pmId+"' AND hdr.TRANSACTION_STATUS in( '"+status+"') AND ssd.SEQUENCE_STATUS = 'DS003'";
                getSalesContDtl = this.jdbcTemplate.query(getQ, new GetSalesContDtlRowMapper(),startDate,endDate, tenantId,empId);
            }
			
			getSalesContDtl.get(0).setUom(uom);
       } catch (Exception ex) {
			logger.error("getSalesContDtl error " + ex.getMessage());
		}
		return getSalesContDtl;
	}
	@Override
	public List<SalesOrderDetailsList> saleMISEnqDtlList(String startDate, String endDate, String tenantId, String empId
			) {
		List<SalesOrderDetailsList> getSalesContDtl = null;

		try {
			String getFilePath=GetPropertyValue.getPropValue("SALES_MIS_STATUS_LIST", tenantId, jdbcTemplate);
			String splitarr[]=  getFilePath.split(",");
			String status ="";
			for(int i =0;i<splitarr.length;i++) {
				if(status.equalsIgnoreCase("")) {
					status = splitarr[i];
				}else {
					status = status +"','"+splitarr[i];
				}
			}
			String getQ;
//			String getQ = " SELECT   \n"
//					+ "						    hdr.SE_ID AS SE_COUNT, case when sbsh.TOTAL_BUDGET_COST is null or 0 then hdr.TENTATIVE_PO_VALUE else (sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,SUBSTRING(ssd.UPDATED_ON,1,7) AS MONTH_YEAR  \n"
//					+ "						FROM  \n"
//					+ "						    sales_enq_hdr hdr  \n"
//					+ "						        INNER JOIN  \n"
//					+ "						    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID  \n"
//					+ "						        LEFT JOIN  \n"
//					+ "						    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS  \n"
//					+ "						        LEFT JOIN  \n"
//					+ "						    stg_master sm ON sm.STG_CODE = hdr.TRANSACTION_STAGE  \n"
//					+ "						        LEFT JOIN  \n"
//					+ "						    document_status_type_code dstc1 ON dstc1.DOCUMENT_STATUS_TYPE_CODE = dtl.TRANSACTION_STATUS  \n"
//					+ "						        LEFT JOIN  \n"
//					+ "						    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID   INNER JOIN sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID \n"
//					+ "						WHERE hdr.CREATED_DATETIME between ? and ? \n"
//					+ "						        AND hdr.TENANT_ID = ? AND pat.TENANT_ID = hdr.TENANT_ID   \n"
//					+ "						        AND pat.ASSIGNED_EMP_ID = ?  \n"
//					+ "						        AND pat.IS_ACTIVE = 1  AND ssd.SS_ID = (SELECT \n"
//					+ "            SS_ID\n"
//					+ "        FROM\n"
//					+ "            sales_status_dtl\n"
//					+ "        WHERE\n"
//					+ "            SEQUENCE_STATUS = 'DS003'\n"
//					+ "                AND REFERENCE_ID = dtl.SE_DTL_ID\n"
//					+ "        ORDER BY UPDATED_ON DESC\n"
//					+ "        LIMIT 1) \n"
//					+ "						        AND pat.PM_ID = 1 AND hdr.TRANSACTION_STATUS in( '"+status+"') AND ssd.SEQUENCE_STATUS = 'DS003' order by hdr.SE_ID ";
//			getSalesContDtl = this.jdbcTemplate.query(getQ,new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,empId);
			if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
                getQ = " SELECT   \n"
    					+ "						    hdr.SE_ID AS SE_COUNT, case when sbsh.TOTAL_BUDGET_COST is null or 0 then hdr.TENTATIVE_PO_VALUE else (sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,SUBSTRING(ssd.UPDATED_ON,1,7) AS MONTH_YEAR  \n"
    					+ "						FROM  \n"
    					+ "						    sales_enq_hdr hdr  \n"
    					+ "						        INNER JOIN  \n"
    					+ "						    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID  \n"
    					+ "						        LEFT JOIN  \n"
    					+ "						    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS  \n"
    					+ "						        LEFT JOIN  \n"
    					+ "						    stg_master sm ON sm.STG_CODE = hdr.TRANSACTION_STAGE  \n"
    					+ "						        LEFT JOIN  \n"
    					+ "						    document_status_type_code dstc1 ON dstc1.DOCUMENT_STATUS_TYPE_CODE = dtl.TRANSACTION_STATUS  \n"
    					+ "						        LEFT JOIN  \n"
    					+ "						    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID   INNER JOIN sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID \n"
    					+ "						WHERE hdr.CREATED_DATETIME between ? and ? \n"
    					+ "						        AND hdr.TENANT_ID = ? AND pat.TENANT_ID = hdr.TENANT_ID   \n"
//    					+ "						        AND pat.ASSIGNED_EMP_ID = ?  \n"
    					+ "						        AND pat.IS_ACTIVE = 1  AND ssd.SS_ID = (SELECT \n"
    					+ "            SS_ID\n"
    					+ "        FROM\n"
    					+ "            sales_status_dtl\n"
    					+ "        WHERE\n"
    					+ "            SEQUENCE_STATUS = 'DS003'\n"
    					+ "                AND REFERENCE_ID = dtl.SE_DTL_ID\n"
    					+ "        ORDER BY UPDATED_ON DESC\n"
    					+ "        LIMIT 1) \n"
    					+ "						        AND pat.PM_ID = 1 AND hdr.TRANSACTION_STATUS in( '"+status+"') AND ssd.SEQUENCE_STATUS = 'DS003' order by hdr.SE_ID ";
    			getSalesContDtl = this.jdbcTemplate.query(getQ,new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId);
            } else {
                getQ = " SELECT   \n"
    					+ "						    hdr.SE_ID AS SE_COUNT, case when sbsh.TOTAL_BUDGET_COST is null or 0 then hdr.TENTATIVE_PO_VALUE else (sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST)  end VAL,SUBSTRING(ssd.UPDATED_ON,1,7) AS MONTH_YEAR  \n"
    					+ "						FROM  \n"
    					+ "						    sales_enq_hdr hdr  \n"
    					+ "						        INNER JOIN  \n"
    					+ "						    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID  \n"
    					+ "						        LEFT JOIN  \n"
    					+ "						    document_status_type_code dstc ON dstc.DOCUMENT_STATUS_TYPE_CODE = hdr.TRANSACTION_STATUS  \n"
    					+ "						        LEFT JOIN  \n"
    					+ "						    stg_master sm ON sm.STG_CODE = hdr.TRANSACTION_STAGE  \n"
    					+ "						        LEFT JOIN  \n"
    					+ "						    document_status_type_code dstc1 ON dstc1.DOCUMENT_STATUS_TYPE_CODE = dtl.TRANSACTION_STATUS  \n"
    					+ "						        LEFT JOIN  \n"
    					+ "						    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID LEFT JOIN sales_budget_sheet_hdr sbsh on sbsh.MASTER_ID = hdr.SE_ID   INNER JOIN sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID \n"
    					+ "						WHERE hdr.CREATED_DATETIME between ? and ? \n"
    					+ "						        AND hdr.TENANT_ID = ? AND pat.TENANT_ID = hdr.TENANT_ID   \n"
    					+ "						        AND pat.ASSIGNED_EMP_ID = ?  \n"
    					+ "						        AND pat.IS_ACTIVE = 1  AND ssd.SS_ID = (SELECT \n"
    					+ "            SS_ID\n"
    					+ "        FROM\n"
    					+ "            sales_status_dtl\n"
    					+ "        WHERE\n"
    					+ "            SEQUENCE_STATUS = 'DS003'\n"
    					+ "                AND REFERENCE_ID = dtl.SE_DTL_ID\n"
    					+ "        ORDER BY UPDATED_ON DESC\n"
    					+ "        LIMIT 1) \n"
    					+ "						        AND pat.PM_ID = 1 AND hdr.TRANSACTION_STATUS in( '"+status+"') AND ssd.SEQUENCE_STATUS = 'DS003' order by hdr.SE_ID ";
    			getSalesContDtl = this.jdbcTemplate.query(getQ,new SalesOrderDetailsListRowMapper(),startDate,endDate, tenantId,empId);
            }
		} catch (Exception ex) {
			logger.error("saleMISEnqDtlList error " + ex.getMessage());
		}
		return getSalesContDtl;
	}
	@Override
	public List<ProjectHdrDtlEntity> getSalesContProjects(String fromDate, String toDate, String tenantId,
			String projectId, String pmId, String empId) {
		List<ProjectHdrDtlEntity>respList = new ArrayList<ProjectHdrDtlEntity>();

		try {
			String projId ="like '%%'";
			
			String getFilePath=GetPropertyValue.getPropValue("SALES_MIS_STATUS_LIST", tenantId, jdbcTemplate);
			String splitarr[]=  getFilePath.split(",");
			String status ="";
			for(int i =0;i<splitarr.length;i++) {
				if(status.equalsIgnoreCase("")) {
					status = splitarr[i];
				}else {
					status = status +"','"+splitarr[i];
				}
			}
            String getQ;
//			String getQ="SELECT \r\n" + 
//					"    ph.PROJECT_CODE, ph.PROJECT_NAME,hdr.SE_ID\r\n" + 
//					"FROM\r\n" + 
//					"    sales_enq_hdr hdr\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    project_hdr ph ON hdr.SE_ID = ph.ENQUIRY_ID\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\r\n" + 
//					"        LEFT JOIN\r\n" + 
//					"    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\r\n" + 
//					"        LEFT JOIN\r\n" + 
//					"    sales_budget_sheet_hdr shdr ON shdr.MASTER_ID = hdr.SE_ID\r\n" + 
//					"        INNER JOIN\r\n" + 
//					"    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
//					"WHERE\r\n" + 
//					"    hdr.CREATED_DATETIME BETWEEN ? AND ? \r\n" + 
//					"        AND hdr.TENANT_ID = ?\r\n" + 
//					"        AND pat.TENANT_ID = hdr.TENANT_ID\r\n" + 
//					"        AND pat.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
//					"        AND pat.IS_ACTIVE = 1\r\n" + 
//					"        AND hdr.SE_ID "+projId+"\r\n" + 
//					"        AND ssd.SS_ID = (SELECT \r\n" + 
//					"            SS_ID\r\n" + 
//					"        FROM\r\n" + 
//					"            sales_status_dtl\r\n" + 
//					"        WHERE\r\n" + 
//					"            SEQUENCE_STATUS = 'DS003'\r\n" + 
//					"                AND REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
//					"        ORDER BY UPDATED_ON DESC\r\n" + 
//					"        LIMIT 1)\r\n" + 
//					"        AND pat.PM_ID = '"+pmId+"'\r\n" + 
//					"        AND hdr.TRANSACTION_STATUS IN ('"+status+"')\r\n" + 
//					"        AND ssd.SEQUENCE_STATUS = 'DS003'";
//			
//			respList = this.jdbcTemplate.query(getQ, new ProjectHdrDtlRowMapper(),fromDate,toDate, tenantId);
            if (empId == null || empId.trim().isEmpty() || "GETALL".equalsIgnoreCase(empId)) {
                getQ = "SELECT DISTINCT\r\n" + 
    					"    ph.PROJECT_CODE, ph.PROJECT_NAME,hdr.SE_ID\r\n" + 
    					"FROM\r\n" + 
    					"    sales_enq_hdr hdr\r\n" + 
    					"        INNER JOIN\r\n" + 
    					"    project_hdr ph ON hdr.SE_ID = ph.ENQUIRY_ID\r\n" + 
    					"        INNER JOIN\r\n" + 
    					"    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\r\n" + 
    					"        LEFT JOIN\r\n" + 
    					"    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\r\n" + 
    					"        LEFT JOIN\r\n" + 
    					"    sales_budget_sheet_hdr shdr ON shdr.MASTER_ID = hdr.SE_ID\r\n" + 
    					"        INNER JOIN\r\n" + 
    					"    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
    					"WHERE\r\n" + 
    					"    hdr.CREATED_DATETIME BETWEEN ? AND ? \r\n" + 
    					"        AND hdr.TENANT_ID = ?\r\n" + 
    					"        AND pat.TENANT_ID = hdr.TENANT_ID\r\n" + 
//    					"        AND pat.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
//                        "        AND pat.ASSIGNED_EMP_ID = ?\r\n" +
    					"        AND pat.IS_ACTIVE = 1\r\n" + 
    					"        AND hdr.SE_ID "+projId+"\r\n" + 
    					"        AND ssd.SS_ID = (SELECT \r\n" + 
    					"            SS_ID\r\n" + 
    					"        FROM\r\n" + 
    					"            sales_status_dtl\r\n" + 
    					"        WHERE\r\n" + 
    					"            SEQUENCE_STATUS = 'DS003'\r\n" + 
    					"                AND REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
    					"        ORDER BY UPDATED_ON DESC\r\n" + 
    					"        LIMIT 1)\r\n" + 
    					"        AND pat.PM_ID = '"+pmId+"'\r\n" + 
    					"        AND hdr.TRANSACTION_STATUS IN ('"+status+"')\r\n" + 
    					"        AND ssd.SEQUENCE_STATUS = 'DS003'";
    			
    			respList = this.jdbcTemplate.query(getQ, new ProjectHdrDtlRowMapper(),fromDate,toDate, tenantId);
            } else {
                getQ = "SELECT \r\n" + 
    					"    ph.PROJECT_CODE, ph.PROJECT_NAME,hdr.SE_ID\r\n" + 
    					"FROM\r\n" + 
    					"    sales_enq_hdr hdr\r\n" + 
    					"        INNER JOIN\r\n" + 
    					"    project_hdr ph ON hdr.SE_ID = ph.ENQUIRY_ID\r\n" + 
    					"        INNER JOIN\r\n" + 
    					"    sales_enq_dtl dtl ON hdr.SE_ID = dtl.MASTER_ID\r\n" + 
    					"        LEFT JOIN\r\n" + 
    					"    process_assigned_team pat ON pat.MASTER_ID = hdr.SE_ID\r\n" + 
    					"        LEFT JOIN\r\n" + 
    					"    sales_budget_sheet_hdr shdr ON shdr.MASTER_ID = hdr.SE_ID\r\n" + 
    					"        INNER JOIN\r\n" + 
    					"    sales_status_dtl ssd ON ssd.REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
    					"WHERE\r\n" + 
    					"    hdr.CREATED_DATETIME BETWEEN ? AND ? \r\n" + 
    					"        AND hdr.TENANT_ID = ?\r\n" + 
    					"        AND pat.TENANT_ID = hdr.TENANT_ID\r\n" + 
//    					"        AND pat.ASSIGNED_EMP_ID = '"+empId+"'\r\n" + 
                        "        AND pat.ASSIGNED_EMP_ID = ?\r\n" +
    					"        AND pat.IS_ACTIVE = 1\r\n" + 
    					"        AND hdr.SE_ID "+projId+"\r\n" + 
    					"        AND ssd.SS_ID = (SELECT \r\n" + 
    					"            SS_ID\r\n" + 
    					"        FROM\r\n" + 
    					"            sales_status_dtl\r\n" + 
    					"        WHERE\r\n" + 
    					"            SEQUENCE_STATUS = 'DS003'\r\n" + 
    					"                AND REFERENCE_ID = dtl.SE_DTL_ID\r\n" + 
    					"        ORDER BY UPDATED_ON DESC\r\n" + 
    					"        LIMIT 1)\r\n" + 
    					"        AND pat.PM_ID = '"+pmId+"'\r\n" + 
    					"        AND hdr.TRANSACTION_STATUS IN ('"+status+"')\r\n" + 
    					"        AND ssd.SEQUENCE_STATUS = 'DS003'";
    			
    			respList = this.jdbcTemplate.query(getQ, new ProjectHdrDtlRowMapper(),fromDate,toDate, tenantId,empId);
            }
		} catch (Exception ex) {
			logger.error("getSalesContProjects error " + ex.getMessage());
		}
		return respList;
	}
//	@Override
//	public List<EmployeeEntity> getSalesDeptEmployees(String tenantId, String depCode) {
//	    List<EmployeeEntity> employeeList = new ArrayList<>();
//	    try {
//	       
//	        String query = "SELECT EMPLOYEE_ID, EMPLOYEE_FIRSTNAME " +  
//	                       "FROM employee_mst " +  
//	                       "WHERE TENANT_ID = ? AND DEPARTMENT_CODE = ?";
//	    
//
//	        
//	        employeeList = this.jdbcTemplate.query(query, new EmployeeRowMapper(), tenantId,depCode);
//
//	       
//	        logger.debug("Employees fetched: " + employeeList);
//
//	    } catch (Exception ex) {
//	        logger.error("getSalesDeptEmployees error: " + ex.getMessage());
//	    }
//	    return employeeList;
//	}
	@Override
	public List<EmployeeEntity> getSalesDeptEmployees(String tenantId, String depCode,String empId,String isHod) {
	    List<EmployeeEntity> employeeList = new ArrayList<>();
	    try {
	      
	    	logger.debug("HOD Check Result: " + isHod);
	        
            String query;
            if ("1".equals(isHod)) {
               
                query = "SELECT EMPLOYEE_ID, EMPLOYEE_FIRSTNAME " +  
                        "FROM employee_mst " +  
                        "WHERE TENANT_ID = ? AND DEPARTMENT_CODE = ?";
                employeeList = this.jdbcTemplate.query(query, new EmployeeRowMapper(), tenantId, depCode);
                
            } else {
                
                query = "SELECT EMPLOYEE_ID, EMPLOYEE_FIRSTNAME " +  
                        "FROM employee_mst " +  
                        "WHERE TENANT_ID = ? AND DEPARTMENT_CODE = ? AND EMPLOYEE_ID = ?";
                employeeList = this.jdbcTemplate.query(query, new EmployeeRowMapper(), tenantId, depCode, empId);
            }
            
            logger.warn("Employees fetched: " + employeeList);

        } catch (Exception ex) {
            logger.error("getSalesDeptEmployees error: " + ex.getMessage());
        }
        return employeeList;
    }
}
