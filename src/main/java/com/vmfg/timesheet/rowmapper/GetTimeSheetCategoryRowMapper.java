package com.vmfg.timesheet.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.timesheet.response.GetTimeSheetCategoryEntity;

public class GetTimeSheetCategoryRowMapper implements RowMapper<GetTimeSheetCategoryEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetTimeSheetCategoryRowMapper.class);

	@Override
	public GetTimeSheetCategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetTimeSheetCategoryEntity result = new GetTimeSheetCategoryEntity();
		try {

			if (columnExists(rs, "TC_NAME")) {
				result.setTcName((rs.getString("TC_NAME")));
			}
			if (columnExists(rs, "CREATED_BY")) {
				result.setCreatedBy((rs.getString("CREATED_BY")));
			}
			if (columnExists(rs, "CREATED_ON")) {
				result.setCreatedOn((rs.getString("CREATED_ON")));
			}
			if (columnExists(rs, "UPDATED_ON")) {
				result.setUpdatedOn((rs.getString("UPDATED_ON")));
			}
			if (columnExists(rs, "UPDATED_BY")) {
				result.setUpdatedBy((rs.getString("UPDATED_BY")));
			}
			if (columnExists(rs, "TC_ID")) {
				result.setTcId((rs.getString("TC_ID")));
			}
			if (columnExists(rs, "CREATED_EMP_NAME")) {
				result.setCreatedEmpName((rs.getString("CREATED_EMP_NAME")));
			}
			if (columnExists(rs, "UPDATED_EMP_NAME")) {
				result.setUpdatedEmpName((rs.getString("UPDATED_EMP_NAME")));
			}

		} catch (Exception ex) {
			logger.error("GetTimeSheetCategoryRowMapper error " + ex);
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
