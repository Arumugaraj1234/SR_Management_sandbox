package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ProductBasedInventoryDtlEntity;

public class ProductBasedInventoryDtlRowMapper implements RowMapper<ProductBasedInventoryDtlEntity>{
	private static final Logger logger = LoggerFactory.getLogger(ProductBasedInventoryDtlRowMapper.class);
	@Override
	public ProductBasedInventoryDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductBasedInventoryDtlEntity res = new ProductBasedInventoryDtlEntity();
		try {

			res.setInventoryLocationCode(rs.getString("INVENTORY_LOCATION_CODE"));
			res.setProductQtyOnHand(rs.getString("PRODUCT_QUANTITY_ON_HAND"));
			res.setInventoryLocationDesc(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
			res.setInventoryLocationType(rs.getString("INVENTORY_LOCATION_TYPE"));
			res.setInventoryLocationParentCode(rs.getString("INVENTORY_LOCATION_PARENT_CODE"));
			

		} catch (Exception ex) {
			logger.error("ProductBasedInventoryDtlRowMapper  Method Exception" + ex);
		}
		return res;
	}


}
