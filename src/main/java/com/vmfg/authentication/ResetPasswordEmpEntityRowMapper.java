package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ResetPasswordEmpEntityRowMapper implements RowMapper<ResetPasswordEmpEntity>{
	private static final Logger logger = LoggerFactory.getLogger(ResetPasswordEmpEntityRowMapper.class);
	@Override
	public ResetPasswordEmpEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		ResetPasswordEmpEntity rpe  = new ResetPasswordEmpEntity();
		try {
			rpe.setUserloginid(row.getInt("user_loginid"));
			rpe.setUserName(row.getString("userName"));
			rpe.setEmployeeid(row.getString("EMPLOYEE_ID"));
			rpe.setEmpFirstName(row.getString("EMPLOYEE_FIRSTNAME"));
			rpe.setIsActive(row.getString("isActive"));
			rpe.setTenantid(row.getString("TENANT_ID"));
			rpe.setEmployeeDesignation(row.getString("DESIGNATION_NAME"));
		} catch (Exception e) {
			logger.error("ResetPasswordEmpEntityRowMapper Method Exception----->" + e);
		}
		return rpe;
	}

}
