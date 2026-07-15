package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ProductQuntyVlRowMapper implements RowMapper<ProductInvQnty> {
	private static final Logger logger = LoggerFactory.getLogger(ProductQuntyVlRowMapper.class);
	@Override
	public ProductInvQnty mapRow(ResultSet row, int rowNum) throws SQLException {
		ProductInvQnty fi = new ProductInvQnty();
		try {
			fi.setProductinvdtlid(row.getInt("INVENTORY_PRODUCT_DTL_ID"));
			fi.setProductQuantityonhand(row.getInt("PRODUCT_QUANTITY_ON_HAND"));
		}catch(Exception ex) {
			logger.error("ProductQuntyVlRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
