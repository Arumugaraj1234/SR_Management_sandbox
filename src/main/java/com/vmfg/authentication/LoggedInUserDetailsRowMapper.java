package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;


public class LoggedInUserDetailsRowMapper implements RowMapper<LoggedInUserDetailsHdr>{
	private static final Logger logger = LoggerFactory.getLogger(LoggedInUserDetailsRowMapper.class);
	@Override
	public LoggedInUserDetailsHdr mapRow(ResultSet rs, int rowNum) throws SQLException {
		LoggedInUserDetailsHdr hdr=new LoggedInUserDetailsHdr();
		try {
			hdr.setEmployeeLoggedInCount(rs.getString("COUNT_LOGGED_IN"));
			hdr.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
			hdr.setEmployeeId(rs.getString("EMPLOYEE_ID"));
		}catch (Exception e) {
			logger.error("LoggedInUserDetailsRowMapper Method Exception----->" + e);
		}
		return hdr;
	}

}
