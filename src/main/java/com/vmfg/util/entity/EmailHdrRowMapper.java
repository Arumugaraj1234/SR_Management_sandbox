package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class EmailHdrRowMapper implements RowMapper<LineProgramInfo> {
	private static final Logger logger = LoggerFactory.getLogger(EmailHdrRowMapper.class);

	@Override
	public LineProgramInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		LineProgramInfo tm = new LineProgramInfo();
		try {
			tm.setLineDesc(row.getString("LINE_MST_DESCRIPTION"));
			tm.setPrgmDesc(row.getString("PROGRAM_DESCRIPTION"));
			tm.setNameDesc(row.getString("NAME_DESC"));

		} catch (Exception e) {
			logger.error("EmailHdrRowMapper Exception--->" + e);
		}
		return tm;
	}
}
