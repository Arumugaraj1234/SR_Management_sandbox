package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.TaskTypeEntity;

public class TasktypeDtlsRowMapper implements RowMapper<TaskTypeEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TasktypeDtlsRowMapper.class);


	@Override
	public TaskTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TaskTypeEntity res = new TaskTypeEntity();
		try {
			if (columnExists(rs, "TT_CODE")) {
				res.setTtCode(rs.getString("TT_CODE"));
			}
			if (columnExists(rs, "TT_DESC")) {
				res.setTtDesc(rs.getString("TT_DESC"));
			}
			if (columnExists(rs, "IS_ACTIVE")) {
				res.setIsActive(rs.getString("IS_ACTIVE"));
			}
		}catch(Exception e) {
			logger.error("TasktypeDtlsRowMapper  Method Exception" + e);
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
