package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.InvProdDrillEntity;

public class InvProdDrillEntityRowMapper implements RowMapper<InvProdDrillEntity> {
	private static final Logger logger = LoggerFactory.getLogger(InvProdDrillEntityRowMapper.class);

	@Override
	public InvProdDrillEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		InvProdDrillEntity lst = new InvProdDrillEntity();
		try {
			lst.setPmHdrId(rs.getString("PM_HDR_ID"));
			lst.setProjectCode(rs.getString("PROJECT_CODE"));
			lst.setProductCode(rs.getString("PRODUCT_CODE"));
			lst.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			lst.setSpecification(rs.getString("SPECIFICATION"));
			lst.setMake(rs.getString("MAKE"));
			lst.setUom(rs.getString("UOM_SHORT_DESCRIPTION"));
			lst.setWeight(rs.getString("WEIGHT"));
			lst.setLocation(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
			lst.setBin(rs.getString("BIN"));
			lst.setQtyOnHand(rs.getString("PRODUCT_QUANTITY_ON_HAND"));
			lst.setCostPerUnit(rs.getString("PRODUCT_COST_PER_UNIT"));
			lst.setInventoryValue(rs.getString("INVENTORY_VALUE"));  // Qty × Cost
			lst.setInwardDateTime(rs.getString("INWARD_DATETIME"));
		} catch (Exception ex) {
			logger.error("InvProdDrillEntityRowMapper Method Exception " + ex);
		}
		return lst;
	}


	

}
