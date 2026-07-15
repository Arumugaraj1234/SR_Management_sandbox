package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.TaskTypeMstEntity;

public class TaskTypeMstRowMapper implements RowMapper<TaskTypeMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskTypeMstRowMapper.class);

	@Override
	public TaskTypeMstEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		TaskTypeMstEntity tm = new TaskTypeMstEntity();
		try {
			tm.setDeptCode(row.getString("DEPT_CODE"));
			tm.setIsActive(row.getString("IS_ACTIVE"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTtCode(row.getString("TT_CODE"));
			tm.setTtDesc(row.getString("TT_DESC"));
			
		} catch (Exception e) {
			logger.error("TaskTypeMstRowMapper Exception--->" + e);
		}
		return tm;
	}


}
