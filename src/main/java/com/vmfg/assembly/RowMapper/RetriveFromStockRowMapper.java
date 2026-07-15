package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.RetriveFromStockEntity;

public class RetriveFromStockRowMapper implements RowMapper<RetriveFromStockEntity> {
	private static final Logger logger = LoggerFactory.getLogger(RetriveFromStockRowMapper.class);

	@Override
	public RetriveFromStockEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		RetriveFromStockEntity result = new RetriveFromStockEntity();
		try {

			if (columnExists(rs, "AVAILABLE_QTY")) {
				result.setAvailableQty(rs.getString("AVAILABLE_QTY"));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				result.setProductode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "PRODUCT_DESCRIPTION")) {
				result.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			}
			if (columnExists(rs, "UOM_LONG_DESCRIPTION")) {
				result.setUomLongDesc(rs.getString("UOM_LONG_DESCRIPTION"));
			}
			if (columnExists(rs, "UOM_SHORT_DESCRIPTION")) {
				result.setUomShortDesc(rs.getString("UOM_SHORT_DESCRIPTION"));
			}
			if (columnExists(rs, "PRODUCT_ID")) {
				result.setProductId(rs.getString("PRODUCT_ID"));
			}
			if (columnExists(rs, "PKA_ID")) {
				result.setPkaId(rs.getString("PKA_ID"));
			}
			if (columnExists(rs, "PK_DESC")) {
				result.setPkDesc(rs.getString("PK_DESC"));
			}
			if (columnExists(rs, "PKSA_ID")) {
				result.setPksaId(rs.getString("PKSA_ID"));
			}
			if (columnExists(rs, "PSK_DESC")) {
				result.setPskDesc(rs.getString("PSK_DESC"));
			}
			if (columnExists(rs, "INVENTORY_LOCATION_DESCRIPTION")) {
				result.setInvLocationDesc(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
			}
			if (columnExists(rs, "INVENTORY_LOCATION_CODE")) {
				result.setInvLocationCode(rs.getString("INVENTORY_LOCATION_CODE"));
			}
			if (columnExists(rs, "SPECIFICATION")) {
				result.setSpecification(rs.getString("SPECIFICATION"));
			}
			if(columnExists(rs, "MAKE")) {
				result.setMake(rs.getString("MAKE"));
			}
		} catch (Exception ex) {
			logger.error("RetriveFromStockRowMapper error " + ex);
		}

		return result;
	}

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
