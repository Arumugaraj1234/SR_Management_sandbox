package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class LineProgramInfoRowMapper implements RowMapper<LineProgramInfo>{
	private static final Logger logger = LoggerFactory.getLogger(LineProgramInfoRowMapper.class);
	@Override
	public LineProgramInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		LineProgramInfo tm = new LineProgramInfo();
		try {
			tm.setLineDesc(row.getString("LINE_MST_DESCRIPTION"));
			tm.setPrgmDesc(row.getString("PROGRAM_DESCRIPTION"));
			tm.setProcess(row.getString("OPERATION_DESCRIPTION"));
			tm.setNameDesc(row.getString("NAME_DESC"));
			
			} catch (Exception e) {
			logger.error("LineProgramInfoRowMapper Exception--->"+e);
		}
		return tm;
	}

}
