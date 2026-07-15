package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.TaskEntryHdrEntity;

public class TaskEntryHdrRowMapper implements RowMapper<TaskEntryHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskEntryHdrRowMapper.class);

	@Override
	public TaskEntryHdrEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		TaskEntryHdrEntity tm = new TaskEntryHdrEntity();
		try {

			tm.setDepartmentCode(row.getString("DEPARTMENT_CODE"));
			tm.setDepartmentDesc(row.getString("DEPARTMENT_NAME")); //
			tm.setDependentTeHdrId(row.getString("DEPENDENT_TE_HDR_ID"));
			tm.setLastUpdatedBy(row.getString("LAST_UPDATED_BY"));
			tm.setLastUpdatedDatatime(row.getString("LAST_UPDATED_DATETIME"));
			tm.setMasterId(row.getString("MASTER_ID"));
			tm.setTaskCategoryCode(row.getString("TASK_CATEGORY_CODE"));
			tm.setTaskCategoryDesc(row.getString("TC_DESC")); //
			tm.setTaskName(row.getString("TASK_NAME"));
			tm.setTaskTypeCode(row.getString("TASK_TYPE_CODE"));
			tm.setTaskTypeDesc(row.getString("TT_DESC"));
			tm.setTtHdrId(row.getString("TT_HDR_ID"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTeHdrId(row.getString("TE_HDR_ID"));
			
		} catch (Exception e) {
			logger.error("TaskEntryHdrRowMapper Exception--->" + e);
		}
		return tm;
	}

}
