package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ProductBasedPoDtlEntity;

public class ProductBasedPoDtlRowMapper implements RowMapper<ProductBasedPoDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProductBasedPoDtlEntity.class);
	@Override
	public ProductBasedPoDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductBasedPoDtlEntity row = new ProductBasedPoDtlEntity();
		try {
		
			if (columnExists(rs, "PO_CODE")) {
				row.setPoCode(rs.getString("PO_CODE"));
			}
			if (columnExists(rs, "QTY")) {
				row.setQty(rs.getString("QTY"));
			}
			if (columnExists(rs, "RECEIVED_QTY")) {
				row.setReceviedQty(rs.getString("RECEIVED_QTY"));
			}
			if (columnExists(rs, "INWARD_QTY")) {
				row.setInwardQty(rs.getString("INWARD_QTY"));
			}
			if (columnExists(rs, "UNITE_RATE")) {
				row.setUnitRate(rs.getString("UNITE_RATE"));
			}
			if (columnExists(rs, "TOTAL_VALUE")) {
				row.setTotValue(rs.getString("TOTAL_VALUE"));
			}
		} catch (Exception e) {
			logger.error("Exception in ProductBasedPoDtlRowMapper" + e);

		}

		return row;
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
