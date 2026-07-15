package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;


public class LoginUserDetailsRowMapper implements RowMapper<LoginUserDetails>{
	private static final Logger logger = LoggerFactory.getLogger(LoginUserDetailsRowMapper.class);
	@Override
	public LoginUserDetails mapRow(ResultSet row, int rowNum) throws SQLException {
		LoginUserDetails lud = new LoginUserDetails();
		try {
			lud.setUserLoginId(row.getInt("USERLOGINID"));
			lud.setUserName(row.getString("USERNAME"));
			lud.setIsactive(row.getInt("ISACTIVE"));
			lud.setEmployeeID(row.getString("EMPLOYEE_ID"));
			lud.setEmployeeFirstName(row.getString("EMPLOYEE_FIRSTNAME"));
			lud.setEmployeeLastName(row.getString("EMPLOYEE_LASTNAME"));
			lud.setEmployeeDesignation(row.getString("DESIGNATION_NAME"));
			lud.setDesignationCode(row.getString("DESIGNATION_CODE"));
			lud.setTenantid(row.getString("TENANT_ID"));
		} catch (Exception e) {
			logger.debug("LoginUserDetailsRowMapper Method Exception"+e);
		}
		return lud;
	}

}
