package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetTaskHdrByEmpIdEntity;

public class GetTaskHdrByEmpIdRowMapper implements RowMapper<GetTaskHdrByEmpIdEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetTaskHdrByEmpIdRowMapper.class);

	@Override
	public GetTaskHdrByEmpIdEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetTaskHdrByEmpIdEntity result = new GetTaskHdrByEmpIdEntity();
		try {

			if (columnExists(rs, "TASK_CATEGORY_CODE")) {
				result.setTaskCategoryCode((rs.getString("TASK_CATEGORY_CODE")));
			}
			if (columnExists(rs, "TC_DESC")) {
				result.setTaskDesc((rs.getString("TC_DESC")));
			}

			

		} catch (Exception ex) {
			logger.error("GetTaskHdrByEmpIdRowMapper error " + ex);
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
