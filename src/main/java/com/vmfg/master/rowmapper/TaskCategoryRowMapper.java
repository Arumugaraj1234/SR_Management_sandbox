package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.TaskCategoryEntity;

public class TaskCategoryRowMapper implements RowMapper<TaskCategoryEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskCategoryRowMapper.class);

	@Override
	public TaskCategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		TaskCategoryEntity ent = new TaskCategoryEntity();
		try {
			ent.setTcCode(rs.getString("TC_CODE"));
			ent.setTcDesc(rs.getString("TC_DESC"));
			ent.setIsActive(rs.getString("IS_ACTIVE"));
		}catch (Exception e) {
			logger.error("TaskCategoryRowMapper method error"+e);
		}
		return ent;
	}

}
