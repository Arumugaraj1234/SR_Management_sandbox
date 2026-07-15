package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.SalesEnqDtlEntity;

public class SalesEnqDtlRowMapper implements RowMapper<SalesEnqDtlEntity>{
	private static final Logger logger = LoggerFactory.getLogger(SalesEnqDtlRowMapper.class);
	@Override
	public SalesEnqDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		SalesEnqDtlEntity se = new SalesEnqDtlEntity();
		try {
			se.setCreatedDateTime(row.getString("CREATED_DATETIME"));
			se.setCustomerName(row.getString("CUSTOMER_NAME"));
			se.setEnquiryCode(row.getString("ENQUIRY_CODE"));
			se.setEnquiryCustomerStatus(row.getString("ENQUIRY_CUSTOMER_STATUS"));
			se.setEnquiryDate(row.getString("ENQUIRY_DATE"));
			se.setEnquiryNo(row.getString("SE_ID"));
			se.setEnquiryType(row.getString("ENQUIRY_TYPE"));
			se.setHdrStatusDesc(row.getString("HDR_STATUS_DESC"));
			se.setIndustrialType(row.getString("INDUSTRIAL_TYPE"));
			se.setLastUpdatedDatetime(row.getString("LAST_UPDATED_DATETIME"));
			se.setLeadDtl(row.getString("LEAD_DTL"));
			se.setProductDetails(row.getString("PRODUCT_DETAILS"));
			se.setProjectDescription(row.getString("PROJECT_DESCRIPTION"));
			se.setProjectName(row.getString("PROJECT_NAME"));
			se.setReason(row.getString("REASON"));
			se.setScopeOfWork(row.getString("SCOPE_OF_WORK"));
			se.setSeId(row.getString("SE_ID"));
			se.setSlaveStatus(row.getString("SLAVE_STATUS"));
			se.setSlaveStatusDesc(row.getString("SLAVE_STATUS_DESC"));
			se.setStgDesc(row.getString("STG_DESC"));
			se.setTenantId(row.getString("TENANT_ID"));
			se.setTentativePoValue(row.getString("TENTATIVE_PO_VALUE"));
			se.setExpectedPoDate(row.getString("EXPECTED_PO_DATE"));
			se.setTransactionStage(row.getString("TRANSACTION_STAGE"));
			se.setTransactionStageSeq(row.getString("TRANSACTION_STAGE_SEQ"));
			se.setTransactionStatus(row.getString("TRANSACTION_STATUS"));
			se.setTransactionStatusSeq(row.getString("TRANSACTION_STATUS_SEQ"));
			se.setLocation(row.getString("LOCATION"));
//			se.setIndustrialDesc(row.getString("IT_DESC"));
//			se.setScopeOfWorkDesc(row.getString("SOW_DESC"));
			if (columnExists(row, "FINAL_COST")) {
				se.setFinalCost(row.getString("FINAL_COST"));
			}
		} catch (Exception e) {
			logger.error("SalesEnqDtlRowMapper Method Exception---->"+e);
		}
		return se;
	}
	private boolean columnExists(ResultSet row, String columnName) throws SQLException {
		ResultSetMetaData metaData = row.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}

}
