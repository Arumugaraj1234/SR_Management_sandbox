package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.TaskEntryRemarksEntity;

public class GetTaskEntryRemarksRowMapper implements RowMapper<TaskEntryRemarksEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskEntryDtlRowMapper.class);

	@Override
	public TaskEntryRemarksEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		TaskEntryRemarksEntity tm = new TaskEntryRemarksEntity();
		try {
			tm.setDateTime(row.getString("LOGGED_DATETIME"));
			tm.setEmpId(row.getString("LOGGED_BY"));
			tm.setEmpName(row.getString("EMPLOYEE_FIRSTNAME"));
			tm.setRemarks(row.getString("REMARKS"));
			tm.setTeDtlId(row.getString("TE_DTl_ID"));
		} catch (Exception e) {
			logger.error("GetTaskEntryRemarksRowMapper Exception--->" + e);
		}
		return tm;
	}
}
