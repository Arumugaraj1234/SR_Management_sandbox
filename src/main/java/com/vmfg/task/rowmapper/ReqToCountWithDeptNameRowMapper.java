package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.ReqToCountWithDeptName;

public class ReqToCountWithDeptNameRowMapper implements RowMapper<ReqToCountWithDeptName> {
	private static final Logger logger = LoggerFactory.getLogger(ReqToCountWithDeptNameRowMapper.class);

	@Override
	public ReqToCountWithDeptName mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReqToCountWithDeptName result = new ReqToCountWithDeptName();
		try {

			if (columnExists(rs, "REQUESTED_TO_DEPT_COUNT")) {
				result.setReqToDeptCount((rs.getString("REQUESTED_TO_DEPT_COUNT")));
			}
			if (columnExists(rs, "DEPARTMENT_NAME")) {
				result.setDepartmentName((rs.getString("DEPARTMENT_NAME")));
			}

		} catch (Exception ex) {
			logger.error("ReqToCountWithDeptNameRowMapper error " + ex);
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
