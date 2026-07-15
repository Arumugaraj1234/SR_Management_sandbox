package com.vmfg.authentication;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class UserScreenByIdRowMapper implements RowMapper<UserScreenMap>{
	private static final Logger logger = LoggerFactory.getLogger(UserScreenByIdRowMapper.class);
	@Override
	public UserScreenMap mapRow(ResultSet row, int rowNum) throws SQLException {
		UserScreenMap usm = new UserScreenMap();
		try {
			usm.setUiScreenMstId(row.getInt("UI_SCREEN_MST_ID"));
			usm.setTenanId(row.getString("TENANT_ID"));
		}catch(Exception ex) {
			logger.error("UserScreenByIdRowMapper map row Exception "+ex);
		}
		return usm;
	}

}
