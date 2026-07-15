package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ProductMstDropDownEntity;

public class productMstDropDownRowMapper implements RowMapper<ProductMstDropDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(productMstDropDownRowMapper.class);

	@Override
	public ProductMstDropDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductMstDropDownEntity res = new ProductMstDropDownEntity();
		try {
			res.setProductCode(rs.getString("PRODUCT_CODE"));
			res.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			res.setSpec(rs.getString("SPECIFICATION"));
			res.setProductId(rs.getString("PRODUCT_ID"));
			res.setPmHdrId(rs.getString("PM_HDR_ID"));
			res.setPkaId(rs.getString("PKA_ID"));
			res.setPskaId(rs.getString("PSKA_ID"));
			res.setUomLongDescriprtion(rs.getString("UOM_LONG_DESCRIPTION"));
			res.setUomShortDescriprtion(rs.getString("UOM_SHORT_DESCRIPTION"));
			res.setMake(rs.getString("MAKE"));
			res.setQty(rs.getString("QTY"));
			res.setUnit(rs.getString("UNIT"));
			res.setWeight(rs.getString("WEIGHT"));
			res.setMaterial(rs.getString("MATERIAL"));
			res.setLastupdatedUserId(rs.getString("LAST_UPDATED_USER_ID"));
			res.setLastupdatedDateTime(rs.getString("LAST_UPDATED_DATETIME"));
			res.setUomCode(rs.getString("PRODUCT_UOM_CODE"));
			
			
		} catch (Exception ex) {
			logger.error("productMstDropDownRowMapper  Method Exception" + ex);
		}
		return res;
	}
}