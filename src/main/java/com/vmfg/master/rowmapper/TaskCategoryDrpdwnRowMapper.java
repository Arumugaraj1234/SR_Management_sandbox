package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.TaskCategoryDrpdwnEntity;

public class TaskCategoryDrpdwnRowMapper implements RowMapper<TaskCategoryDrpdwnEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskCategoryDrpdwnRowMapper.class);

	@Override
	public TaskCategoryDrpdwnEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		TaskCategoryDrpdwnEntity res =  new TaskCategoryDrpdwnEntity();
		try {
			if (columnExists(rs, "TC_CODE")) {
				res.setTcCode(rs.getString("TC_CODE"));
			}
			if (columnExists(rs, "TC_DESC")) {
				res.setTcDesc(rs.getString("TC_DESC"));
			}
			
		}catch(Exception e) {
			logger.error("TaskCategoryDrpdwnRowMapper  Method Exception" + e);
		}
		return res;
	}

	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

	for (int i = 1; i <= columns; i++) {
		if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
			return true;
		}
	 }
	return false;
	}

}
