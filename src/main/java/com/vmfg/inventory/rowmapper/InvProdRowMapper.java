package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.InvProdEntity;

public class InvProdRowMapper implements RowMapper<InvProdEntity> {
	private static final Logger logger = LoggerFactory.getLogger(InvProdRowMapper.class);

	@Override
	public InvProdEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		InvProdEntity res = new InvProdEntity();
		try {

			if (columnExists(rs, "BIN")) {
				res.setBin(rs.getString("BIN"));
			}
			if (columnExists(rs, "PRODUCT_COST_PER_UNIT")) {
				res.setUnitRate(rs.getString("PRODUCT_COST_PER_UNIT"));
			}
			if (columnExists(rs, "PRODUCT_ID")) {
				res.setProductId(rs.getString("PRODUCT_ID"));
			}
			if (columnExists(rs, "PM_HDR_ID")) {
				res.setProjectId(rs.getString("PM_HDR_ID"));
			}
			if (columnExists(rs, "CUSTOMER_NAME")) {
				res.setCustomerName(rs.getString("CUSTOMER_NAME"));
			}
			if (columnExists(rs, "WEIGHT")) {
				res.setWeight(rs.getString("WEIGHT"));
			}
			if (columnExists(rs, "UOM_SHORT_DESCRIPTION")) {
				res.setUom(rs.getString("UOM_SHORT_DESCRIPTION"));
			}
			if (columnExists(rs, "PRODUCT_QUANTITY_ON_HAND")) {
				res.setQtyOnHand(rs.getString("PRODUCT_QUANTITY_ON_HAND"));
			}
			if (columnExists(rs, "UOM_SHORT_DESCRIPTION")) {
				res.setUom(rs.getString("UOM_SHORT_DESCRIPTION"));
			}
			if (columnExists(rs, "PROJECT_CODE")) {
				res.setProjCode(rs.getString("PROJECT_CODE"));
			}
			if (columnExists(rs, "PRODUCT_DESCRIPTION")) {
				res.setProdDesc(rs.getString("PRODUCT_DESCRIPTION"));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				res.setProdCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "INVENTORY_LOCATION_DESCRIPTION")) {
				res.setLocationDesc(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
			}
			if(columnExists(rs, "SPECIFICATION")) {
				res.setSpecification(rs.getString("SPECIFICATION"));
			}
			if(columnExists(rs, "MAKE")) {
				res.setMake(rs.getString("MAKE"));
			}
			if(columnExists(rs, "PO_CODE")) {
				res.setPoCode(rs.getString("PO_CODE"));
			}
			if(columnExists(rs, "INVENTORY_VALUE")) {
				res.setInvValue(rs.getString("INVENTORY_VALUE"));
			}
			
			
		} catch (Exception e) {
			logger.error("InvProdRowMapper  Method Exception" + e);
		}
		return res;
	}
	// column checking purpose (column is there or not)
		private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();

			for (int i = 1; i <= columns; i++) {
				if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
					return true;
				}
			}

			return false;
		}

}
