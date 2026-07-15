package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ProductInventoryRowMapper implements RowMapper<ProductInventoryDetails>{
	private static final Logger logger = LoggerFactory.getLogger(ProductInventoryRowMapper.class);

	@Override
	public ProductInventoryDetails mapRow(ResultSet row, int rowNum) throws SQLException {
		ProductInventoryDetails pd = new ProductInventoryDetails();
		try {
			pd.setProductCode(row.getString("PRODUCT_CODE"));
			pd.setUnitCount(row.getString("UNIT_COUNT"));
		}catch(Exception ex) {
			logger.error("ProductInventoryRowMapper Exception :"+ex);
		}
		return pd;
	}

}
