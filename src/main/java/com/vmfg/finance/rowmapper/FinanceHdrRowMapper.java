package com.vmfg.finance.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.finance.entity.FinanceHdrEntity;

public class FinanceHdrRowMapper implements RowMapper<FinanceHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(FinanceHdrRowMapper.class);
	@Override
	public FinanceHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		FinanceHdrEntity finance = new FinanceHdrEntity();
		try {
			finance.setFeHdrId(rs.getString("FE_HDR_ID"));
			finance.setPmHdrId(rs.getString("PM_HDR_ID"));
			finance.setProjectName(rs.getString("PROJECT_NAME"));
			finance.setProjectDescription(rs.getString("PROJECT_DESCRIPTION"));
			finance.setProjectCode(rs.getString("PROJECT_CODE"));
			finance.setProductDetails(rs.getString("PRODUCT_DETAILS"));
			finance.setCustomerName(rs.getString("CUSTOMER_NAME"));
			finance.setRequestedBy(rs.getString("REQUESTED_BY"));
			finance.setInitiatedDate(rs.getString("INITIATED_DATE"));
			finance.setTransactionStage(rs.getString("TRANSACTION_STAGE"));
			finance.setHdrStatusDesc(rs.getString("TRANSACTION_STATUS"));
			finance.setDueDate(rs.getString("DUE_DATE"));
			finance.setHandoverDate(rs.getString("PROJECT_HANDOVER_DATE"));
			finance.setEnquiryId(rs.getString("SE_ID"));
			finance.setIsInternal(rs.getString("IS_INTERNAL"));
		} catch (Exception e) {
			logger.error("Exception in FinanceHdrRowMapper" + e);

		}

		return finance;
	}
}
