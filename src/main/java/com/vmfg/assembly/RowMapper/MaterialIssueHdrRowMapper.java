package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.MaterialIssueHdrEntity;

public class MaterialIssueHdrRowMapper implements RowMapper<MaterialIssueHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(MaterialIssueHdrRowMapper.class);

	@Override
	public MaterialIssueHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MaterialIssueHdrEntity result = new MaterialIssueHdrEntity();
		try {

			if (columnExists(rs, "MR_CODE")) {
				result.setMrCode(rs.getString("MR_CODE"));
			}
			if (columnExists(rs, "MI_CODE")) {
				result.setMiCode(rs.getString("MI_CODE"));
			}
			if (columnExists(rs, "MI_DATE")) {
				result.setMiDate(rs.getString("MI_DATE"));
			}
			if (columnExists(rs, "MI_HDR_ID")) {
				result.setMiHdrId(rs.getString("MI_HDR_ID"));
			}
			if (columnExists(rs, "ISSUED_ON")) {
				result.setIssuedOn(rs.getString("ISSUED_ON"));
			}
			if (columnExists(rs, "ISSUED_BY")) {
				result.setIssuedBy(rs.getString("ISSUED_BY"));
			}
			if (columnExists(rs, "MR_HDR_ID")) {
				result.setMrHdrId(rs.getString("MR_HDR_ID"));
			}
			if (columnExists(rs, "REQUESTED_ON")) {
				result.setRequestedOn(rs.getString("REQUESTED_ON"));
			}
			if (columnExists(rs, "IS_COMPLETED")) {
				result.setIsCompleted(rs.getString("IS_COMPLETED"));
			}
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
			if (columnExists(rs, "PROJECT_CODE")) {
				result.setProjCode(rs.getString("PROJECT_CODE"));
			}
			if (columnExists(rs, "REQUESTED_BY")) {
				result.setRequestedBy(rs.getString("REQUESTED_BY"));
			}
		} catch (Exception ex) {
			logger.error("MaterialReqHdrRowMapper error " + ex);
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
