package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class InventrylocCodesRowMapper implements RowMapper<InventrylocCodesentity> {
	private static final Logger logger = LoggerFactory.getLogger(InventrylocCodesRowMapper.class);
	@Override
	public InventrylocCodesentity mapRow(ResultSet row, int rowNum) throws SQLException {
		InventrylocCodesentity fi = new InventrylocCodesentity();
		try {
			fi.setLocationcode(row.getString("INVENTORY_LOCATION_CODE"));
			fi.setInventorylochdrid(row.getInt("INVENTORY_PRODUCT_HDR_ID"));
			fi.setProductquntyonhand(row.getInt("PRODUCT_QUANTITY_ON_HAND"));
		}catch(Exception ex) {
			logger.error("InventrylocCodesRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
