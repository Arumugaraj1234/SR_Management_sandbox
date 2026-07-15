package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.ProductBinEntity;

public class ProductBinRowMapper implements RowMapper<ProductBinEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProductBinRowMapper.class);

	@Override
	public ProductBinEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductBinEntity res = new ProductBinEntity();
		try {
			res.setProductId(rs.getString("PRODUCT_ID"));
			res.setBin(rs.getString("BIN"));
		} catch (Exception ex) {
			logger.error("ProductBinRowMapper Method Exception" + ex);
		}
		return res;
	}
}
