package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.TaskCategoryMstEntity;

public class TaskCategoryMstRowMapper implements RowMapper<TaskCategoryMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskCategoryMstRowMapper.class);

	@Override
	public TaskCategoryMstEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		TaskCategoryMstEntity tm = new TaskCategoryMstEntity();
		try {
			tm.setIsActive(row.getString("IS_ACTIVE"));
			tm.setTcCode(row.getString("TC_CODE"));
			tm.setTcDesc(row.getString("TC_DESC"));
			tm.setTenantId(row.getString("TENANT_ID"));
			
		} catch (Exception e) {
			logger.error("TaskCategoryMstRowMapper Exception--->" + e);
		}
		return tm;
	}

}
