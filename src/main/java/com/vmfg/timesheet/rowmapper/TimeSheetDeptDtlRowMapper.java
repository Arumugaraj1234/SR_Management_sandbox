package com.vmfg.timesheet.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.timesheet.response.TimeSheetDeptDtlEntity;

public class TimeSheetDeptDtlRowMapper implements RowMapper<TimeSheetDeptDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TimeSheetDeptDtlRowMapper.class);

	@Override
	public TimeSheetDeptDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TimeSheetDeptDtlEntity result = new TimeSheetDeptDtlEntity();
		try {

			if (columnExists(rs, "DEPARTMENT_CODE")) {
				result.setDepartmentCode((rs.getString("DEPARTMENT_CODE")));
			}
			if (columnExists(rs, "DEPARTMENT_NAME")) {
				result.setDepartmentName((rs.getString("DEPARTMENT_NAME")));
			}
			if (columnExists(rs, "HOURS")) {
				result.setHours((rs.getString("HOURS")));
			}
			if (columnExists(rs, "RUPEES")) {
				result.setRupees((rs.getString("RUPEES")));
			}
			if (columnExists(rs, "percentage_share_hrs")) {
				result.setPercentageOfHrs((rs.getString("percentage_share_hrs")));
			}
			if (columnExists(rs, "percentage_share_rupees")) {
				result.setPercentageOfRupees((rs.getString("percentage_share_rupees")));
			}

		} catch (Exception ex) {
			logger.error("TimeSheetDeptDtlRowMapper error " + ex);
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
