package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.MaterialReturnAcceptEntity;

public class MaterialReturnAcceptRowMapper implements RowMapper<MaterialReturnAcceptEntity> {
	private static final Logger logger = LoggerFactory.getLogger(MaterialReturnAcceptRowMapper.class);

	@Override
	public MaterialReturnAcceptEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MaterialReturnAcceptEntity result = new MaterialReturnAcceptEntity();
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
			if (columnExists(rs, "EMPLOYEE_ID")) {
				result.setEmpId(rs.getString("EMPLOYEE_ID"));
			}

		} catch (Exception ex) {
			logger.error("MaterialReturnAcceptRowMapper error " + ex);
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
