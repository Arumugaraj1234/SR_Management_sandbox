package com.vmfg.timesheet.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.timesheet.response.TaskEntryHdrAndDtlEntity;

public class TaskEntryHdrAndDtlRowMapper implements RowMapper<TaskEntryHdrAndDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskEntryHdrAndDtlRowMapper.class);

	@Override
	public TaskEntryHdrAndDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TaskEntryHdrAndDtlEntity result = new TaskEntryHdrAndDtlEntity();
		try {

			if (columnExists(rs, "ACTIVITY_NAME")) {
				result.setActivityName((rs.getString("ACTIVITY_NAME")));
			}
			if (columnExists(rs, "ASSIGNED_TO")) {
				result.setAssignToDesc((rs.getString("ASSIGNED_TO")));
			}
			if (columnExists(rs, "QTY")) {
				result.setQty((rs.getString("QTY")));
			}
			if (columnExists(rs, "TC_DESC")) {
				result.setTcDesc((rs.getString("TC_DESC")));
			}
			if (columnExists(rs, "TE_DTl_ID")) {
				result.setTeDtlId((rs.getString("TE_DTl_ID")));
			}
			if (columnExists(rs, "TT_DESC")) {
				result.setTtDesc((rs.getString("TT_DESC")));
			}
			if (columnExists(rs, "TT_DTL_ID")) {
				result.setTtDtlId((rs.getString("TT_DTL_ID")));
			}
			if (columnExists(rs, "TE_HDR_ID")) {
				result.setTeHdrId((rs.getString("TE_HDR_ID")));
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
