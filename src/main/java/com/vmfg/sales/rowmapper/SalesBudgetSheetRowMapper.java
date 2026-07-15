package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.SalesBudgetSheetHdrEntity;

public class SalesBudgetSheetRowMapper implements RowMapper<SalesBudgetSheetHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SalesBudgetSheetRowMapper.class);

	@Override
	public SalesBudgetSheetHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		SalesBudgetSheetHdrEntity entity = new SalesBudgetSheetHdrEntity();

		try {
			if (columnExists(rs, "SB_HDR_ID")) {
				entity.setSbHdrId(rs.getString("SB_HDR_ID"));
			}
			if (columnExists(rs, "TOTAL_BUDGET_COST")) {
				entity.setTotalBudgetCost(rs.getString("TOTAL_BUDGET_COST"));
			}
			if (columnExists(rs, "PAYMENT_TERMS")) {
				entity.setPaymentTerms(rs.getString("PAYMENT_TERMS"));
			}
			if (columnExists(rs, "TRANSACTION_STATUS")) {
				entity.setTransactionStatus(rs.getString("TRANSACTION_STATUS"));
			}
			if (columnExists(rs, "TRANSACTION_STATUS_SEQ")) {
				entity.setTransactionStatusSeq(rs.getString("TRANSACTION_STATUS_SEQ"));
			}
			entity.setSalePercent(rs.getString("SALE_PERCENT"));
			entity.setSaleValue(rs.getString("FINAL_SALE_VALUE"));
			entity.setCrCost(rs.getString("CR_COST"));
			entity.setCrfinalCost(rs.getString("FINAL_CR_SALE_COST"));
			entity.setCrsalePercent(rs.getString("CR_SALE_PERCENT"));
		} catch (Exception ex) {
			logger.error("SalesBudgetSheetRowMapper  Method Exception" + ex);

		}
		return entity;
	}

	// column checking purpose (column is there or not)
	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
				return true;
			}
		}

		return false;
	}

}
