package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.TaskTypeDropDownEntity;

public class TaskTypeDropDownRowMapper implements RowMapper<TaskTypeDropDownEntity>{
	
	private static final Logger logger = LoggerFactory.getLogger(TaskTypeDropDownRowMapper.class);

	@Override
	public TaskTypeDropDownEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		TaskTypeDropDownEntity ent = new TaskTypeDropDownEntity();
		try {
			ent.setTtCode(rs.getString("TT_CODE"));
			ent.setTtDesc(rs.getString("TT_DESC"));
		}catch (Exception e) {
			logger.error("TaskTypeDropDownRowMapper method Error"+e);
		}
		return ent;
	}

}
