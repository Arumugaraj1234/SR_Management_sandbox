package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.SalesBudgetFullEntity;

public class SalesBudgetFullRowMapper implements RowMapper<SalesBudgetFullEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SalesBudgetFullRowMapper.class);

	@Override
	public SalesBudgetFullEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		SalesBudgetFullEntity tm = new SalesBudgetFullEntity();
		try {
		
			
			tm.setFinalSalesVal(row.getString("FINAL_SALE_VALUE"));
			tm.setMasterId(row.getString("MASTER_ID"));
			tm.setPaymentTerms(row.getString("PAYMENT_TERMS"));
			tm.setSalePercent(row.getString("SALE_PERCENT"));
			tm.setSbHdrId(row.getString("SB_HDR_ID"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTotalBudgetCost(row.getString("TOTAL_BUDGET_COST"));
			tm.setTransactionSeq(row.getString("TRANSACTION_STATUS_SEQ"));
			tm.setTransactionStatus(row.getString("TRANSACTION_STATUS"));

		} catch (Exception e) {
			logger.error("SalesBudgetFullRowMapper Exception--->" + e);
		}
		return tm;
	}

}
