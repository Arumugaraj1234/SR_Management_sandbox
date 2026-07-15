package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OeeByLineRowMapper implements RowMapper<OeeByLineInfo>{
	private static final Logger logger = LoggerFactory.getLogger(OeeByLineRowMapper.class);
	@Override
	public OeeByLineInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		OeeByLineInfo oe = new OeeByLineInfo();
		try {
			oe.setLineCode(row.getString("LINE_MST_CODE"));
			oe.setLineDescription(row.getString("LINE_MST_DESCRIPTION"));
		}catch(Exception ex) {
			logger.error("OeeByLineRowMapper map row exception -->"+ex);
		}
		return oe;
	}

}
