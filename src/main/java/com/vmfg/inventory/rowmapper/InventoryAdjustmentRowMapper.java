package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.InventoryAdjustmentEntity;

public class InventoryAdjustmentRowMapper implements RowMapper<InventoryAdjustmentEntity> {
	private static final Logger logger = LoggerFactory.getLogger(InventoryAdjustmentRowMapper.class);

	@Override
	public InventoryAdjustmentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		InventoryAdjustmentEntity entity = new InventoryAdjustmentEntity();
		try {
			entity.setSno(rs.getString("SERIAL_NUMBER"));
			entity.setAdjustmentCode(rs.getString("INV_ADJUSTMENT_CODE"));
			entity.setProjectCode(rs.getString("PROJECT_CODE"));
	        entity.setProjectDesc(rs.getString("PROJECT_DESCRIPTION"));
	        entity.setProductCode(rs.getString("PRODUCT_CODE"));
	        entity.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
	        entity.setUom(rs.getString("UOM"));
	        entity.setLocationCode(rs.getString("INVENTORY_LOCATION_CODE"));
	        entity.setLocationDesc(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
	        entity.setAdjustmentType(rs.getString("ADJUSTMENT_TYPE"));
	        entity.setAdjustedBy(rs.getString("ADJUSTED_BY"));
	        entity.setAdjustedDateTime(rs.getString("ADJUSTMENT_RECORDED_DATE"));
	        entity.setQtyonHand(rs.getString("PRODUCT_PRIOR_QUANTITY_ON_HAND"));
	        entity.setAdjustmentQty(rs.getString("PRODUCT_ADJUSTED_QUANTITY"));
	        entity.setRevisedqtyonHand(rs.getString("PRODUCT_REVISED_QUANTITY_ON_HAND"));
	        entity.setReason(rs.getString("ADJUSTMENT_REASON"));
		}catch (Exception e) {
			logger.error("InventoryAdjustmentRowMapper  Method Exception" + e);
		}
		return entity;
	}

}
