package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.ProcessAssignedTeamEntity;

public class ProcessAssignedTeamRowMapper implements RowMapper<ProcessAssignedTeamEntity>{
	private static final Logger logger = LoggerFactory.getLogger(ProcessAssignedTeamRowMapper.class);
	@Override
	public ProcessAssignedTeamEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		ProcessAssignedTeamEntity processAssignedTeamEntity=new ProcessAssignedTeamEntity();
		try {
			if (columnExists(rs, "DEPARTMENT_NAME")) {
				processAssignedTeamEntity.setEmployeeDept(rs.getString("DEPARTMENT_NAME"));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				processAssignedTeamEntity.setEmployeeName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}
			if (columnExists(rs, "IS_ACTIVE")) {
				processAssignedTeamEntity.setIsActive(rs.getString("IS_ACTIVE"));
			}
			if (columnExists(rs, "EMPLOYEE_ID")) {
				processAssignedTeamEntity.setEmpId(rs.getString("EMPLOYEE_ID"));
			}


		}catch(Exception ex) {
			logger.error("ProcessAssignedTeamRowMapper  Method Exception" + ex);

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
