package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.TaskTemplateDtlEntity;

public class TaskTemplateDtlRowMapper implements RowMapper<TaskTemplateDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskTemplateDtlRowMapper.class);

	@Override
	public TaskTemplateDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		TaskTemplateDtlEntity tm = new TaskTemplateDtlEntity();
		try {
	
			tm.setActivityName(row.getString("ACTIVITY_NAME"));
			tm.setIsActive(row.getString("IS_ACTIVE"));
			tm.setLastUpdatedBy(row.getString("LAST_UPDATED_BY"));
			tm.setLastUpdatedtime(row.getString("LAST_UPDATED_DATETIME"));
			tm.setPlannedDurationDays(row.getString("PLANNED_DURATION_DAYS"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTtDtlId(row.getString("TT_DTL_ID"));
			tm.setTtHdrId(row.getString("TT_HDR_ID"));
			tm.setSno(row.getInt("Serial_Number"));
		} catch (Exception e) {
			logger.error("TaskEntryHdrRowMapper Exception--->" + e);
		}
		return tm;

}
}
