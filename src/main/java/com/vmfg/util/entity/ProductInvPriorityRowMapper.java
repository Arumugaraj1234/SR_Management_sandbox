package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ProductInvPriorityRowMapper implements RowMapper<ProductInvPriority> {
	private static final Logger logger = LoggerFactory.getLogger(ProductInvPriorityRowMapper.class);
	@Override
	public ProductInvPriority mapRow(ResultSet row, int rowNum) throws SQLException {
		ProductInvPriority fi = new ProductInvPriority();
		try {
			fi.setInventoryloccode(row.getString("INVENTORY_LOCATION_CODE"));
		}catch(Exception ex) {
			logger.error("ProductInvPriorityRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
