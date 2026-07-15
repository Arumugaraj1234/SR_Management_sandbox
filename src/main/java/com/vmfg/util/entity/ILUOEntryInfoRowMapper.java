package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ILUOEntryInfoRowMapper implements RowMapper<ILUOEntryInfo> {
	private static final Logger logger = LoggerFactory.getLogger(ILUOEntryInfoRowMapper.class);

	@Override
	public ILUOEntryInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		ILUOEntryInfo tm = new ILUOEntryInfo();
		try {
			tm.setOperatorName(row.getString("EMPLOYEE_FIRSTNAME"));
			tm.setProcess(row.getString("DESCRIPTION"));
		} catch (Exception e) {
			logger.error("ILUOEntryInfoRowMapper Exception--->" + e);
		}
		return tm;
	}
}
