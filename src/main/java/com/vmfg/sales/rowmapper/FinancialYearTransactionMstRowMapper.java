package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.FinancialYearTransactionMstEntity;

public class FinancialYearTransactionMstRowMapper implements RowMapper<FinancialYearTransactionMstEntity>{
	private static final Logger logger = LoggerFactory.getLogger(FinancialYearTransactionMstRowMapper.class);
	@Override
	public FinancialYearTransactionMstEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		FinancialYearTransactionMstEntity se = new FinancialYearTransactionMstEntity();
		try {
		
			se.setPmID(row.getString("PM_ID"));
			se.setFytId(row.getString("FINANCIAL_YEAR_TRANSACTION_MST_ID"));
			se.setIsActive(row.getString("IS_ACTIVE"));
			se.setPrefixCode(row.getString("PREFIX_IDENTIFIER"));
			se.setStartId(row.getString("ID_START"));
			se.setSuffixCode(row.getString("SUFFIX_IDENTIFIER"));
			
		} catch (Exception e) {
			logger.error("FinancialYearTransactionMstRowMapper Method Exception---->"+e);
		}
		return se;
	}

}
