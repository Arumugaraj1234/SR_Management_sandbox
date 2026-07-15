package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class FinancialYearMstRowMapper implements RowMapper<FinancialYearMst>{
	private static final Logger logger = LoggerFactory.getLogger(FinancialYearMstRowMapper.class);
	@Override
	public FinancialYearMst mapRow(ResultSet row, int rowNum) throws SQLException {
		FinancialYearMst tm = new FinancialYearMst();
		try {
			tm.setFinancialYear(row.getString("FINANCIAL_YEAR"));
			tm.setStartDate(row.getString("START_DATE"));
			tm.setEndDate(row.getString("END_DATE"));
			tm.setFinancialYearId(row.getString("FINANCIAL_YEAR_MST_ID"));
			tm.setIsactive(row.getString("IS_ACTIVE"));
		} catch (Exception e) {
			logger.error("FinancialYearMstRowMapper Exception--->"+e);
		}
		return tm;
	}

}
