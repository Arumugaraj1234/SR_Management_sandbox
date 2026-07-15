package com.vmfg.timesheet.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.timesheet.response.TimeSheetMonthDtlEntity;

public class TimeSheetMonthDtlRowMapper implements RowMapper<TimeSheetMonthDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TimeSheetMonthDtlRowMapper.class);

	@Override
	public TimeSheetMonthDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TimeSheetMonthDtlEntity result = new TimeSheetMonthDtlEntity();
		try {

			if (columnExists(rs, "MONTHS")) {
				result.setMonth((rs.getString("MONTHS")));
			}
			if (columnExists(rs, "TOTAL_HOURS")) {
				result.setHours((rs.getString("TOTAL_HOURS")));
			}

		} catch (Exception ex) {
			logger.error("TimeSheetMonthDtlRowMapper error " + ex);
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