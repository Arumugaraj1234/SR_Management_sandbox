package com.vmfg.timesheet.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.timesheet.response.GetTimeSheetDtlEntity;

public class GetTimeSheetDtlRowMapper implements RowMapper<GetTimeSheetDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetTimeSheetDtlRowMapper.class);

	@Override
	public GetTimeSheetDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetTimeSheetDtlEntity result = new GetTimeSheetDtlEntity();
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
			if (columnExists(rs, "DEPARTMENT_CODE")) {
				result.setDepartmentCode((rs.getString("DEPARTMENT_CODE")));
			}
			if (columnExists(rs, "DEPARTMENT_NAME")) {
				result.setDepartmentDesc((rs.getString("DEPARTMENT_NAME")));
			}
			if (columnExists(rs, "TIMESHEET_DTL")) {
				result.setTimeSheetDtl((rs.getString("TIMESHEET_DTL")));
			}
			if (columnExists(rs, "RECORDED_ON")) {
				result.setRecordedOn((rs.getString("RECORDED_ON")));
			}
			if (columnExists(rs, "PROJECT_DESCRIPTION")) {
				result.setProjectDesc((rs.getString("PROJECT_DESCRIPTION")));
			}
			if (columnExists(rs, "PROJECT_CODE")) {
				result.setProjectCode((rs.getString("PROJECT_CODE")));
			}
			if (columnExists(rs, "PM_HDR_ID")) {
				result.setPmHdrId((rs.getString("PM_HDR_ID")));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
				result.setProjectName((rs.getString("PROJECT_NAME")));
			}
			if (columnExists(rs, "TIMESHEET_HRS")) {
				result.setTimeSheetHrs((rs.getString("TIMESHEET_HRS")));
			}
			if (columnExists(rs, "RECORD_DATE")) {
				result.setRecordDate((rs.getString("RECORD_DATE")));
			}
			if (columnExists(rs, "T_DTL_ID")) {
				result.setTDtlId((rs.getString("T_DTL_ID")));
			}
			if(columnExists(rs, "TIMESHEET_CATEGORY")) {
				result.setTimeSheetCategory((rs.getString("TIMESHEET_CATEGORY")));
			}
			if(columnExists(rs, "SUMMARY")) {
				result.setSummary((rs.getString("SUMMARY")));
			}
			if(columnExists(rs, "TYPE")) {
				result.setType((rs.getString("TYPE")));
			}
			if(columnExists(rs, "CATEGORY")) {
				result.setCategory((rs.getString("CATEGORY")));
			}
			if(columnExists(rs, "TE_DTL_ID")) {
				result.setTaskEntryId((rs.getString("TE_DTL_ID")));
			}
			if(columnExists(rs, "TASK_ENTRY_DESC")) {
				result.setTaskEntryDesc((rs.getString("TASK_ENTRY_DESC")));
			}

		} catch (Exception ex) {
			logger.error("GetTimeSheetDtlRowMapper error " + ex);
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
