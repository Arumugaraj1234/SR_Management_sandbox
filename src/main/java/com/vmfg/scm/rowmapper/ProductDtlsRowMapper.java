package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.ProductDtlsEntity;

public class ProductDtlsRowMapper implements RowMapper<ProductDtlsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProductDtlsRowMapper.class);

	@Override

	public ProductDtlsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductDtlsEntity product = new ProductDtlsEntity();
		try {
			product.setProductId(rs.getString("PRODUCT_ID"));
			product.setSpecification(rs.getString("SPECIFICATION"));
			product.setMake(rs.getString("MAKE"));
			product.setUom(rs.getString("PRODUCT_UOM_CODE"));
			product.setMaterial(rs.getString("MATERIAL"));
			product.setProdDesc(rs.getString("PRODUCT_DESCRIPTION"));
		} catch (Exception e) {
			logger.error("ProductDtlsRowMapper Exception--->" + e);
		}
		return product;
	}
}
