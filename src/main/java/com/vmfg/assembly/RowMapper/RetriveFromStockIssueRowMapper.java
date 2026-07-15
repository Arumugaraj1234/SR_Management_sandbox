package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.RetriveFromStockIssueEntity;

public class RetriveFromStockIssueRowMapper implements RowMapper<RetriveFromStockIssueEntity> {
	private static final Logger logger = LoggerFactory.getLogger(RetriveFromStockIssueRowMapper.class);

	@Override
	public RetriveFromStockIssueEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		RetriveFromStockIssueEntity result = new RetriveFromStockIssueEntity();
		try {

			if (columnExists(rs, "INVENTORY_QTY")) {
				result.setInventoryQty(rs.getString("INVENTORY_QTY"));
			}
			if (columnExists(rs, "REQUIRED_QTY")) {
				result.setRequierdQty(rs.getString("REQUIRED_QTY"));
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
			if (columnExists(rs, "MR_DTL_ID")) {
				result.setMrDtlId(rs.getString("MR_DTL_ID"));
			}
			if(columnExists(rs, "BIN")) {
				result.setBin(rs.getString("BIN"));
			}
			
		} catch (Exception ex) {
			logger.error("RetriveFromStockIssueRowMapper error " + ex);
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
