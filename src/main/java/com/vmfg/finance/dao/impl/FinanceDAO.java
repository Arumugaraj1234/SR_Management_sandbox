package com.vmfg.finance.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vmfg.design.dao.impl.DesignDAO;
import com.vmfg.finance.dao.interfaces.IFinanceDAO;
import com.vmfg.finance.entity.FinanceHdrEntity;
import com.vmfg.finance.rowmapper.FinanceHdrRowMapper;
@Repository
public class FinanceDAO implements IFinanceDAO {
	private static final Logger logger = LoggerFactory.getLogger(DesignDAO.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	@Override
	public List<FinanceHdrEntity> getFinanceDtl(String fromDate, String toDate, String customer, String processId, String empId,
			String tenantID, String financeId,String projectId) {
		logger.debug("getFinanceDtl DAO method start");
		List<FinanceHdrEntity> hdr = null;
		try {
			String custname = "";
			if (customer.equalsIgnoreCase("")) {
				custname = "%%";
			} else {
				custname = "%" + customer + "%";
			}

			String datediff="";
			if(!fromDate.equalsIgnoreCase("")) {
				datediff = "  fh.INITIATED_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"' AND";
			}
			String pro = " and fh.PM_HDR_ID like '%%' ";
			if(!projectId.equalsIgnoreCase("getall")) {
				pro = " and fh.PM_HDR_ID = '"+projectId+"'";
			}
			
			if (financeId.isEmpty()) {
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
						"    proj.DUE_DATE,sales.IS_INTERNAL\r\n" + 
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
						"        AND pat.IS_ACTIVE = 1\r\n" + 
						"        AND fh.TENANT_ID = ?\r\n"+pro;

				hdr = this.jdbcTemplate.query(getQ, new FinanceHdrRowMapper(),empId ,processId,tenantID);
			} else {
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
						"    proj.DUE_DATE,sales.IS_INTERNAL\r\n" + 
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
						"    sales_enq_hdr sales ON proj.ENQUIRY_ID = sales.SE_ID\r\n" + 
						"WHERE\r\n" + 
						"    FE_HDR_ID = ? AND fh.TENANT_ID = ?";

				hdr = this.jdbcTemplate.query(getQ, new FinanceHdrRowMapper(),financeId, tenantID);
			}

		} catch (Exception ex) {
			logger.error("Error with method getFinanceDtl " + ex.getMessage());
		}
		logger.debug("getFinanceDtl DAO method end");
		return hdr;
	}

	


}
