package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ProductMstTableRowMapper implements RowMapper<ProductMstTableEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProductMstTableRowMapper.class);

	@Override
	public ProductMstTableEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		ProductMstTableEntity entity = new ProductMstTableEntity();
		try {
			entity.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			entity.setProductUomCode(rs.getString("PRODUCT_UOM_CODE"));
			entity.setTenatid(rs.getString("TENANT_ID"));
			entity.setProdCode(rs.getString("PRODUCT_CODE"));
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ProductMstTableRowMapper  Error" + e);
		}
		return entity;
	}

}
