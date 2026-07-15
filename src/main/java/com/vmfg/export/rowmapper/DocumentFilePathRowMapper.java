package com.vmfg.export.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vmfg.export.entity.DocumentFilePathEntity;

public class DocumentFilePathRowMapper implements RowMapper<DocumentFilePathEntity> {

	@Override
	public DocumentFilePathEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DocumentFilePathEntity DocufilePath = new DocumentFilePathEntity();
		if (columnExists(rs, "INDENT_DTL_ID")) {
			DocufilePath.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
		}

		return DocufilePath;
	}

	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
				return true;
			}
		}
		return false;
	}

}
