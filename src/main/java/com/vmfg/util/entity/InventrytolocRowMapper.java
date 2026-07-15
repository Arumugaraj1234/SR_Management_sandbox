package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class InventrytolocRowMapper implements RowMapper<Inventrytolocentity> {
	private static final Logger logger = LoggerFactory.getLogger(InventrytolocRowMapper.class);
	@Override
	public Inventrytolocentity mapRow(ResultSet row, int rowNum) throws SQLException {
		Inventrytolocentity fi = new Inventrytolocentity();
		try {
			fi.setTolocationcode(row.getString("INVENTORY_LOCATION_CODE"));
		}catch(Exception ex) {
			logger.error("InventrytolocRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
