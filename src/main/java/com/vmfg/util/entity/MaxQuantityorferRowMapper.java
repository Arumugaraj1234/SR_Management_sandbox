package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class MaxQuantityorferRowMapper implements RowMapper<MaxQuantityorferentity> {
	private static final Logger logger = LoggerFactory.getLogger(locationinvntryRowMapper.class);
	@Override
	public MaxQuantityorferentity mapRow(ResultSet row, int rowNum) throws SQLException {
		MaxQuantityorferentity fi = new MaxQuantityorferentity();
		try {
			fi.setMaximumorderquantity(row.getInt("MAXIMUM_ORDER_QUANTITY"));
		}catch(Exception ex) {
			logger.error("locationinvntryRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
