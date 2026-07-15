package com.vmfg.timesheet.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.timesheet.response.GetEmpIDinTimesheetResponse;

public class GetEmpIDinTimesheetRowMapper implements RowMapper<GetEmpIDinTimesheetResponse> {
	private static final Logger logger = LoggerFactory.getLogger(GetEmpIDinTimesheetRowMapper.class);

	@Override
	public GetEmpIDinTimesheetResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetEmpIDinTimesheetResponse result = new GetEmpIDinTimesheetResponse();
		try {

			if (columnExists(rs, "EMPLOYEE_ID")) {
				result.setEmpId((rs.getString("EMPLOYEE_ID")));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				result.setEmpName((rs.getString("EMPLOYEE_FIRSTNAME")));
			}
			if (columnExists(rs, "EMPLOYEE_CODE")) {
				result.setEmpCode((rs.getString("EMPLOYEE_CODE")));
			}
			

		} catch (Exception ex) {
			logger.error("GetEmpIDinTimesheetRowMapper error " + ex);
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