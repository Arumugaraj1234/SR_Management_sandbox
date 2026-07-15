package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;


public class LoginUserDtlRowMapper implements RowMapper<LoginUserInfoHdr>{
	private static final Logger logger = LoggerFactory.getLogger(LoginUserDtlRowMapper.class);
	@Override
	public LoginUserInfoHdr mapRow(ResultSet rs, int rowNum) throws SQLException {
		LoginUserInfoHdr hdr=new LoginUserInfoHdr();
		try {
			hdr.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
			hdr.setLoginTime(rs.getString("LOGGED_IN_TIME"));
			hdr.setEmpId(rs.getString("EMPLOYEE_ID"));
		}catch (Exception e) {
			logger.error("LoginUserDtlRowMapper Method Exception----->" + e);
		}
		return hdr;
	}

}
