package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class UserRolesRowMapper implements RowMapper<UserRoles>{
	private static final Logger logger = LoggerFactory.getLogger(UserRolesRowMapper.class);

	@Override
	public UserRoles mapRow(ResultSet row, int rowNum) throws SQLException {
		UserRoles ur = new UserRoles();
		try {
			ur.setUserLoginID(row.getInt("USERLOGINID"));
			ur.setUserRoleID(row.getInt("USERROLEID"));
			ur.setRoleCode(row.getString("ROLECODE"));
			ur.setRoleName(row.getString("ROLENAME"));
			ur.setRoleDescription(row.getString("ROLEDESCRIPTION"));
			ur.setTenandID(row.getString("TENANT_ID"));
		} catch (Exception ex) {
			logger.debug("UserRolesRowMapper mapRow Method Exception---->"+ex);
		}
		return ur;
	}

}
