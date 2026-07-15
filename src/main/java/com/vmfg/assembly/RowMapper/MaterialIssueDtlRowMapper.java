package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.MaterialIssueDtlEntity;

public class MaterialIssueDtlRowMapper implements RowMapper<MaterialIssueDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(MaterialIssueDtlRowMapper.class);

	@Override
	public MaterialIssueDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MaterialIssueDtlEntity result = new MaterialIssueDtlEntity();
		try {

			if (columnExists(rs, "REQUESTED_QTY")) {
				result.setRequestedQty(rs.getString("REQUESTED_QTY"));
			}
			if (columnExists(rs, "PRODUCT_DESCRIPTION")) {
				result.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
			}
			if (columnExists(rs, "PRODUCT_CODE")) {
				result.setProductCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "UOM_LONG_DESCRIPTION")) {
				result.setUomLongDesc(rs.getString("UOM_LONG_DESCRIPTION"));
			}
			if (columnExists(rs, "UOM_SHORT_DESCRIPTION")) {
				result.setUomShortDesc(rs.getString("UOM_SHORT_DESCRIPTION"));
			}
			if (columnExists(rs, "AVAILABLE_QTY")) {
				result.setAvailableQty(rs.getString("AVAILABLE_QTY"));
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
