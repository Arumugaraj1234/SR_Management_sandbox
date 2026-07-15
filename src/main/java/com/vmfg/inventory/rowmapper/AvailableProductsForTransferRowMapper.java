package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ProductMstDropDownEntity;

public class AvailableProductsForTransferRowMapper implements RowMapper<ProductMstDropDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(AvailableProductsForTransferRowMapper.class);

	@Override
	public ProductMstDropDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductMstDropDownEntity res = new ProductMstDropDownEntity();
		try {
			res.setProductId(rs.getString("PRODUCT_ID"));
			res.setProductCode(rs.getString("PRODUCT_CODE"));
			res.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			res.setSpec(rs.getString("SPECIFICATION"));
			res.setPmHdrId(rs.getString("PM_HDR_ID"));
			res.setQty(rs.getString("QTY"));
			res.setBin(rs.getString("BIN"));
		} catch (Exception ex) {
			logger.error("AvailableProductsForTransferRowMapper Method Exception" + ex);
		}
		return res;
	}
}
