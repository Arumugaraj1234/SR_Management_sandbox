package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class locationinvntryRowMapper implements RowMapper<locationinvntryentity> {
	private static final Logger logger = LoggerFactory.getLogger(locationinvntryRowMapper.class);
	@Override
	public locationinvntryentity mapRow(ResultSet row, int rowNum) throws SQLException {
		locationinvntryentity fi = new locationinvntryentity();
		try {
			fi.setInventorylocationcode(row.getString("INVENTORY_LOCATION_CODE"));
			fi.setInventoryproducthdrid(row.getInt("INVENTORY_PRODUCT_HDR_ID"));
		}catch(Exception ex) {
			logger.error("locationinvntryRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
