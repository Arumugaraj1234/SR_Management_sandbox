package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class lastUpdatedValueRowMapper implements RowMapper<lastUpdatedValueentity> {
	private static final Logger logger = LoggerFactory.getLogger(lastUpdatedValueRowMapper.class);
	@Override
	public lastUpdatedValueentity mapRow(ResultSet row, int rowNum) throws SQLException {
		lastUpdatedValueentity fi = new lastUpdatedValueentity();
		try {
			fi.setPicklistid(row.getInt("PL_HDR_ID"));
		}catch(Exception ex) {
			logger.error("lastUpdatedValueRowMapper map row exception -->"+ex);
		}
		return fi;
	}
}
