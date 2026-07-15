package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.GetInspTypeEntity;

public class GetInspTypeRowMapper implements RowMapper<GetInspTypeEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetInspTypeRowMapper.class);

	@Override
	public GetInspTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetInspTypeEntity result = new GetInspTypeEntity();
		try {

			if (columnExists(rs, "INSPECTION_TYPE")) {
				result.setInspectionType((rs.getString("INSPECTION_TYPE")));
			}

		} catch (Exception ex) {
			logger.error("GetInspTypeRowMapper error " + ex);
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
