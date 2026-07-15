package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class StandardOperationInfoRowMapper implements RowMapper<LineProgramInfo> {
	private static final Logger logger = LoggerFactory.getLogger(StandardOperationInfoRowMapper.class);

	@Override
	public LineProgramInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		LineProgramInfo tm = new LineProgramInfo();
		try {
			tm.setLineDesc(row.getString("LINE_MST_DESCRIPTION"));
			tm.setNameDesc(row.getString("EMPLOYEE_FIRSTNAME"));
			tm.setProcess(row.getString("OPERATION_DESCRIPTION"));

		} catch (Exception e) {
			logger.error("StandardOperationInfoRowMapper Exception--->" + e);
		}
		return tm;
	}
}
