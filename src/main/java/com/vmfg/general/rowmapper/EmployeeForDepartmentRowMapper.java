package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.EmployeeForDepartmentEntity;

public class EmployeeForDepartmentRowMapper implements RowMapper<EmployeeForDepartmentEntity>{
	private static final Logger logger = LoggerFactory.getLogger(EmployeeForDepartmentRowMapper.class);
	@Override
	public EmployeeForDepartmentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		EmployeeForDepartmentEntity processAssignedTeamEntity=new EmployeeForDepartmentEntity();
		try {
			if (columnExists(rs, "EMPLOYEE_ID")) {
				processAssignedTeamEntity.setEmployeeId(rs.getString("EMPLOYEE_ID"));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				processAssignedTeamEntity.setEmployeeName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}
		}catch(Exception ex) {
			logger.error("EmployeeForDepartmentEntity  Method Exception" + ex);

		}
		return processAssignedTeamEntity;
	}



	//column checking purpose (column is there or not)
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
