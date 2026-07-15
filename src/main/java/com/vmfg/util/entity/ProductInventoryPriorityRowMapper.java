package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ProductInventoryPriorityRowMapper implements RowMapper<ProductInventoryPriorityDetails>{
	private static final Logger logger = LoggerFactory.getLogger(ProductInventoryPriorityRowMapper.class);

	@Override
	public ProductInventoryPriorityDetails mapRow(ResultSet row, int rowNum) throws SQLException {
		ProductInventoryPriorityDetails pd = new ProductInventoryPriorityDetails();
		try {
			pd.setInventoryLocationCode(row.getString("INVENTORY_LOCATION_TYPE_CODE"));
			pd.setPickListType(row.getString("PICKLIST_TYPE_CODE"));
		}catch(Exception ex) {
			logger.error("ProductInventoryPriorityRowMapper method exception:"+ex);
		}
		return pd;
	}

}
