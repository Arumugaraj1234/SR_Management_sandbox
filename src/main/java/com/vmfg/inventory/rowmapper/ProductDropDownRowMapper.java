package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ProductMstDropDownEntity;

public class ProductDropDownRowMapper implements RowMapper<ProductMstDropDownEntity>{
	private static final Logger logger = LoggerFactory.getLogger(ProductDropDownRowMapper.class);

	@Override
	public ProductMstDropDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductMstDropDownEntity res = new ProductMstDropDownEntity();
		try {
			res.setProductCode(rs.getString("PRODUCT_CODE"));
			res.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			res.setProductId(rs.getString("PRODUCT_ID"));
			res.setQty(rs.getString("PRODUCT_QUANTITY_ON_HAND"));
			res.setLocationCode(rs.getString("INVENTORY_LOCATION_CODE"));
		}catch (Exception e) {
			logger.error("ProductDropDownRowMapper  Method Exception" + e);
		}
		return res;
	}

}
