package com.vmfg.sales.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.sales.dao.interfaces.ILandingPageDAO;
import com.vmfg.sales.entity.CustomerMstEntity;
import com.vmfg.sales.entity.SalesEnqContactEntity;
import com.vmfg.sales.entity.SalesEnqDtlEntity;
import com.vmfg.sales.rowmapper.CustomerMstRowMapper;
import com.vmfg.sales.rowmapper.SalesEnqContactRowMapper;
import com.vmfg.sales.rowmapper.SalesEnqDtlRowMapper;
@Transactional
@Repository
public class LandingPageDAO implements ILandingPageDAO{
	private static final Logger logger = LoggerFactory.getLogger(LandingPageDAO.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<SalesEnqDtlEntity> saleEnqDtlBydate(String fromDate, String toDate, String customerName,String tenantId,String empId,String tentativeVal,String isexpectedDate) {
		
		List<SalesEnqDtlEntity> saleEnqDtl = new ArrayList<SalesEnqDtlEntity>();
		try {
			String custname="",dateDiff="";
			if(customerName.equalsIgnoreCase("")) {
				custname = "%%";
			}else {
				custname = "%"+customerName+"%";
			}
			String expectedCheck = "";
			if(isexpectedDate.equalsIgnoreCase("1")){
				expectedCheck = "hdr.EXPECTED_PO_DATE, ";
			}
			if(!fromDate.equalsIgnoreCase("")) {
				dateDiff = " hdr.ENQUIRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' AND ";
			}
				
			String saleEnqDtlStr="SELECT \r\n" + 
					"    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS HDR_STATUS_DESC,\r\n" + 
					"    dstc1.DOCUMENT_STATUS_TYPE_DESCRIPTION AS SLAVE_STATUS_DESC,\r\n" + 
					"    sm.STG_DESC,\r\n" + 
					"    dtl.TRANSACTION_STATUS_SEQ AS SLAVE_STATUS,\r\n" + 
			//		"    hdr.*, case when sbsh.TOTAL_BUDGET_COST is null or 0 then hdr.TENTATIVE_PO_VALUE else sbsh.FINAL_SALE_VALUE end FINAL_COST\r\n" + 
					"    hdr.*, case when sbsh.TOTAL_BUDGET_COST is null or 0 then hdr.TENTATIVE_PO_VALUE else (sbsh.FINAL_SALE_VALUE + sbsh.FINAL_CR_SALE_COST) end FINAL_COST\r\n" + 
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
					"        AND pat.PM_ID = 1 order by "+expectedCheck+" hdr.SE_ID";
			saleEnqDtl = this.jdbcTemplate.query(saleEnqDtlStr, new SalesEnqDtlRowMapper(),custname,tenantId,empId,tentativeVal);
		}catch(Exception ex) {
			logger.error("saleEnqDtlBydate Error" + ex);
		}
		return saleEnqDtl;
	}

	@Override
	public List<SalesEnqDtlEntity> saleEnqDtlByslaveId(String slaveId) {
		List<SalesEnqDtlEntity> saleEnqDtl = new ArrayList<SalesEnqDtlEntity>();
		try {		
				
			String saleEnqDtlStr="SELECT \r\n" + 
					"    dstc.DOCUMENT_STATUS_TYPE_DESCRIPTION AS HDR_STATUS_DESC,\r\n" + 
					"    dstc1.DOCUMENT_STATUS_TYPE_DESCRIPTION AS SLAVE_STATUS_DESC,\r\n" + 
					"    sm.STG_DESC,\r\n" + 
					"    dtl.TRANSACTION_STATUS_SEQ AS SLAVE_STATUS,\r\n" + 
					"    hdr.*\r\n" + 
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
					"WHERE\r\n" + 
					"    dtl.SE_DTL_ID =?";
			saleEnqDtl = this.jdbcTemplate.query(saleEnqDtlStr, new SalesEnqDtlRowMapper(),slaveId);
		}catch(Exception ex) {
			logger.error("saleEnqDtlByslaveId Error" + ex);
		}
		return saleEnqDtl;
	}

	@Override
	public List<SalesEnqContactEntity> getSalesContactDtl(String slaveId) {
		List<SalesEnqContactEntity> saleEnqContact = new ArrayList<SalesEnqContactEntity>();
		try{
			
			String saleEnqContactStr = "select * from sales_enq_contact where MASTER_ID=? order by IS_PRIMARY desc";
			saleEnqContact= this.jdbcTemplate.query(saleEnqContactStr,new SalesEnqContactRowMapper(),slaveId);
		}catch(Exception ex) {
			logger.error("getSalesContactDtl Error "+ ex);
		}
		return saleEnqContact;
	}

	@Override
	public List<CustomerMstEntity> customerMstList(String customerName, String tenantId) {
		List<CustomerMstEntity> customerMstlist = new ArrayList<CustomerMstEntity>();
		try{
			
			String customerMstlistStr = "select * from customer_mst where CUST_NAME = ? AND TENANT_ID = ? ";
			customerMstlist= this.jdbcTemplate.query(customerMstlistStr,new CustomerMstRowMapper(),customerName,tenantId);
		}catch(Exception ex) {
			logger.error("customerMstList Error "+ ex);
		}
		return customerMstlist;
	}

	@Override
	public String getEmloyeeNames(String seId , String tenantId) {
		String returnNames="";
		try {
			String empQry="SELECT \r\n" + 
					"    GROUP_CONCAT(mst.EMPLOYEE_FIRSTNAME SEPARATOR ', ') AS Concatenated_EmployeeIDs\r\n" + 
					"FROM\r\n" + 
					"    sales_enq_hdr hdr\r\n" + 
					"        INNER JOIN\r\n" + 
					"    process_assigned_team proHdr ON hdr.SE_ID = proHdr.MASTER_ID\r\n" + 
					"        INNER JOIN\r\n" + 
					"    employee_mst mst ON mst.EMPLOYEE_ID = proHdr.ASSIGNED_EMP_ID\r\n" + 
					"WHERE\r\n" + 
					"    hdr.SE_ID = ? AND PM_ID = '1' AND hdr.TENANT_ID = ?";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(empQry, seId, tenantId);
			returnNames = resultMap.get("Concatenated_EmployeeIDs").toString();
		}catch(Exception e) {
			logger.info("getEmloyeeNames Method Error"+e);
		}
		return returnNames;
	}

	@Override
	public String customerMstContactList(String secId) {
		String number="";
		try {
			String empQry="select CONTACT_NO from sales_enq_contact where SEC_ID= ? and IS_PRIMARY = '1'";
			Map<String,Object> resultMap = jdbcTemplate.queryForMap(empQry, secId);
			number = resultMap.get("CONTACT_NO").toString();
		}catch(Exception e) {
			logger.info("customerMstContactList Method Error"+e);
		}
		return number;
	}

}
