package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class TenantPropertyMstRowMapper implements RowMapper<TenantPropertyMst>{
	private static final Logger logger = LoggerFactory.getLogger(TenantPropertyMstRowMapper.class);
	@Override
	public TenantPropertyMst mapRow(ResultSet row, int rowNum) throws SQLException {
		TenantPropertyMst tm = new TenantPropertyMst();
		try {
			tm.setPropertyName(row.getString("PROPERTY_NAME"));
			tm.setPropertyValue(row.getString("PROPERTY_VALUE"));
		} catch (Exception e) {
			logger.error("TenantPropertyMstRowMapper Exception--->"+e);
		}
		return tm;
	}

}
