package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class InventoryQuantityRowMapper implements RowMapper<InventoryQuantityDetails>{
	private static final Logger logger = LoggerFactory.getLogger(InventoryQuantityRowMapper.class);

	@Override
	public InventoryQuantityDetails mapRow(ResultSet row, int rowNum) throws SQLException {
		InventoryQuantityDetails id = new InventoryQuantityDetails();
		try {
			id.setInventoryLocationMax(row.getString("INV_LOCATION_MAX"));
			id.setMinimumOrderQuantity(row.getString("MINIMUM_ORDER_QUANTITY"));
			id.setSafetyStock(row.getString("SAFETY_STOCK"));
			id.setProductQuantityOnHand(row.getString("PRODUCT_QUANTITY_ON_HAND"));
			id.setInventoryDtlId(row.getString("INVENTORY_PRODUCT_DTL_ID"));
			id.setInventoryHdrId(row.getString("INVENTORY_PRODUCT_HDR_ID"));
		}catch(Exception ex) {
			logger.error("InventoryQuantityRowMapper Method Exception :"+ex);
		}
		return id;
	}

}
