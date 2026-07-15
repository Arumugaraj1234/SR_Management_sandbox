package com.vmfg.timesheet.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.timesheet.response.TimeSheetRequestsEntity;

public class TimeSheetRequestsRowMapper implements RowMapper<TimeSheetRequestsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TimeSheetRequestsRowMapper.class);

	@Override
	public TimeSheetRequestsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TimeSheetRequestsEntity result = new TimeSheetRequestsEntity();
		try {
			

			if (columnExists(rs, "PROJECT_NAME")) {
				result.setProjectName((rs.getString("PROJECT_NAME")));
			}
			if (columnExists(rs, "PROJECT_CODE")) {
				result.setProjectCode((rs.getString("PROJECT_CODE")));
			}
			if (columnExists(rs, "RECORD_DATE")) {
				result.setRecordDate((rs.getString("RECORD_DATE")));
			}
			if (columnExists(rs, "ACTIVITY")) {
				result.setActivity((rs.getString("ACTIVITY")));
			}
			if (columnExists(rs, "EMPLOYEE_CODE")) {
				result.setEmployeeCode((rs.getString("EMPLOYEE_CODE")));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				result.setEmployeeName((rs.getString("EMPLOYEE_FIRSTNAME")));
			}
			if (columnExists(rs, "HOURS")) {
				result.setHrs((rs.getString("HOURS")));
			}
			if (columnExists(rs, "RUPEES")) {
				result.setRupees((rs.getString("RUPEES")));
			}
			if (columnExists(rs, "T_DTL_ID")) {
				result.setTimeSheetDtlId((rs.getString("T_DTL_ID")));
			}
			if (columnExists(rs, "SUMMARY")) {
				result.setSummary((rs.getString("SUMMARY")));
			}

		} catch (Exception ex) {
			logger.error("TimeSheetRequestsRowMapper error " + ex);
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