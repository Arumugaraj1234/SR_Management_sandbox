package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class UserLoginRowMapper implements RowMapper<UserLogin>{
	private static final Logger logger = LoggerFactory.getLogger(UserLoginRowMapper.class);

	@Override
	public UserLogin mapRow(ResultSet row, int rowNum) throws SQLException {
		logger.debug(" UserLoginRowMapper mapRow method Start");
		UserLogin userLogin = new UserLogin();
		try {
			userLogin.setLoginUserID(row.getInt("USERLOGINID"));	
			userLogin.setUserName(row.getString("USERNAME"));
			userLogin.setEmpID(row.getString("EMPLOYEE_ID"));
			userLogin.setEmpFirstNane(row.getString("EMPLOYEE_FIRSTNAME"));
			userLogin.setEmpLastName(row.getString("EMPLOYEE_LASTNAME"));
			userLogin.setTenantID(row.getString("TENANT_ID"));
			userLogin.setEmployeeDesignation(row.getString("DESIGNATION_NAME"));
			userLogin.setEmpDesignationCode(row.getString("DESIGNATION_CODE"));
			userLogin.setDepCode(row.getString("DEPARTMENT_CODE"));
			userLogin.setEmpDepDesc(row.getString("DEPT_NAME"));
			userLogin.setRoleCode(row.getString("ROLECODE"));
			userLogin.setRoleid(row.getString("USERROLEID"));
			userLogin.setRoleName(row.getString("ROLENAME"));
		logger.debug("UserLoginRowMapper mapRow method end");
		}catch(Exception ex) {
			logger.debug("UserLoginRowMapper mapRow Method Exception"+ex);
		}
		return userLogin;
	}

}
