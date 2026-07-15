package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class LoginUserRolesRowMapper implements RowMapper<LoginUserRoles>{
	private static final Logger logger = LoggerFactory.getLogger(LoginUserRolesRowMapper.class);
	@Override
	public LoginUserRoles mapRow(ResultSet row, int rowNum) throws SQLException {
		LoginUserRoles lur = new LoginUserRoles();
		try {
			lur.setUserRoleId(row.getInt("USERROLEID"));
			lur.setRoleCode(row.getString("ROLECODE"));
			lur.setRoleName(row.getString("ROLENAME"));
			lur.setRoleDescription(row.getString("ROLEDESCRIPTION"));
			lur.setActive(row.getBoolean("ISACTIVE"));
		} catch (Exception e) {
			logger.error("LoginUserRolesRowMapper Method Exception:--->"+e);
		}
		return lur;
	}

}
