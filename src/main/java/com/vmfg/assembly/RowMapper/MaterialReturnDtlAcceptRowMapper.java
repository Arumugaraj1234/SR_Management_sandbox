package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.MaterialReturnDtlAcceptEntity;

public class MaterialReturnDtlAcceptRowMapper implements RowMapper<MaterialReturnDtlAcceptEntity> {
	private static final Logger logger = LoggerFactory.getLogger(MaterialReturnDtlAcceptRowMapper.class);

	@Override
	public MaterialReturnDtlAcceptEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MaterialReturnDtlAcceptEntity result = new MaterialReturnDtlAcceptEntity();
		try {

			if (columnExists(rs, "PM_HDR_ID")) {
				result.setProjectId(rs.getString("PM_HDR_ID"));
			}

			if (columnExists(rs, "QTY")) {
				result.setQty(rs.getString("QTY"));
			}

			if (columnExists(rs, "PRODUCT_CODE")) {
				result.setProductCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "PRODUCT_ID")) {
				result.setProductId(rs.getString("PRODUCT_ID"));
			}
			if (columnExists(rs, "MRH_ID")) {
				result.setMrHdrId(rs.getString("MRH_ID"));
			}
			if (columnExists(rs, "TENANT_ID")) {
				result.setTenantId(rs.getString("TENANT_ID"));
			}
			if (columnExists(rs, "MS_HDR_ID")) {
				result.setMsHdrId(rs.getString("MS_HDR_ID"));
			}

		} catch (Exception ex) {
			logger.error("MaterialReturnDtlAcceptRowMapper error " + ex);
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
