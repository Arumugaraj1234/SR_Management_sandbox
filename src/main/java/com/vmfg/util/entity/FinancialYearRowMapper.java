package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class FinancialYearRowMapper implements RowMapper<FinancialYearMst>{
	private static final Logger logger = LoggerFactory.getLogger(FinancialYearRowMapper.class);
	@Override
	public FinancialYearMst mapRow(ResultSet row, int rowNum) throws SQLException {
		FinancialYearMst fi = new FinancialYearMst();
		try {
			fi.setStartDate(row.getString("START_DATE"));
			fi.setEndDate(row.getString("END_DATE"));
		}catch(Exception ex) {
			logger.error("FinancialYearRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
