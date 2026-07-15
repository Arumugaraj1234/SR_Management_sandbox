package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ProductInvHdrIdRowMapper implements RowMapper<ProductInvHdrId> {
	private static final Logger logger = LoggerFactory.getLogger(ProductInvHdrIdRowMapper.class);
	@Override
	public ProductInvHdrId mapRow(ResultSet row, int rowNum) throws SQLException {
		ProductInvHdrId fi = new ProductInvHdrId();
		try {
			fi.setProductinvhdrid(row.getInt("INVENTORY_PRODUCT_HDR_ID"));
		}catch(Exception ex) {
			logger.error("CmnProductRmMapRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
