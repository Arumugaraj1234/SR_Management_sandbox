package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.MaterialReqDtlEntity;

public class MaterialReqDtlRowMapper implements RowMapper<MaterialReqDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(MaterialReqDtlRowMapper.class);

	@Override
	public MaterialReqDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MaterialReqDtlEntity result = new MaterialReqDtlEntity();
		try {

			if (columnExists(rs, "REQUESTED_QTY")) {
				result.setRequestedQty(rs.getString("REQUESTED_QTY"));
			}

			if (columnExists(rs, "AVAILABLE_QTY")) {
				result.setAvailableQty(rs.getString("AVAILABLE_QTY"));
			}
			if (columnExists(rs, "STATION")) {
				result.setStation(rs.getString("STATION"));
			}
			if (columnExists(rs, "SUBASSY")) {
				result.setSubAssy(rs.getString("SUBASSY"));
			}
			if (columnExists(rs, "PRODUCT_DESCRIPTION")) {
				result.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				result.setProdoctCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "UOM_LONG_DESCRIPTION")) {
				result.setUomLongDesc(rs.getString("UOM_LONG_DESCRIPTION"));
			}
			if (columnExists(rs, "UOM_SHORT_DESCRIPTION")) {
				result.setUomShortDesc(rs.getString("UOM_SHORT_DESCRIPTION"));
			}
			result.setInvenLocation(rs.getString("INVENTORY_LOCATION_DESCRIPTION"));
			result.setIsCancelled(rs.getString("IS_CANCELLED"));
			result.setBin(rs.getString("BIN"));
			if (columnExists(rs, "SPECIFICATION")) {
				result.setSpecification(rs.getString("SPECIFICATION"));
			}
			if (columnExists(rs, "MAKE")) {
				result.setMake(rs.getString("MAKE"));
			}
			if (columnExists(rs, "IS_COMPLETED")) {
				result.setIsCompleted(rs.getString("IS_COMPLETED"));
			}
			if (columnExists(rs, "ISSUED_QTY")) {
				result.setIssuedQty(rs.getString("ISSUED_QTY"));
			}
		} catch (Exception ex) {
			logger.error("MaterialReqDtlRowMapper error " + ex);
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
