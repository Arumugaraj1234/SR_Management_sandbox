package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.DepartmentInfoEntity;

public class DepartmentInfoRowMapper implements RowMapper<DepartmentInfoEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentInfoRowMapper.class);

	@Override
	public DepartmentInfoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		DepartmentInfoEntity departmentInfoEntity = new DepartmentInfoEntity();
		try {
			if (columnExists(rs, "DEPARTMENT_CODE")) {
				departmentInfoEntity.setDepartmentCode(rs.getString("DEPARTMENT_CODE"));
			}
			if (columnExists(rs, "DEPARTMENT_NAME")) {
				departmentInfoEntity.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
			}
			if (columnExists(rs, "DEPARTMENT_EMAIL")) {
				departmentInfoEntity.setDepartmentEmail(rs.getString("DEPARTMENT_EMAIL"));
			}
			if (columnExists(rs, "DEPARTMENT_PHONE")) {
				departmentInfoEntity.setDepartmentPhone(rs.getString("DEPARTMENT_PHONE"));
			}

		} catch (Exception ex) {
			logger.error("DepartmentInfoRowMapper  Method Exception" + ex);

		}
		return departmentInfoEntity;
	}

	// column checking purpose (column is there or not)
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
