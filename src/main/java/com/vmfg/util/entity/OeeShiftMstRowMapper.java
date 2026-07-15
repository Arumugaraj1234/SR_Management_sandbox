package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OeeShiftMstRowMapper implements RowMapper<OeeShiftMstInfo>{
	private static final Logger logger = LoggerFactory.getLogger(OeeShiftMstRowMapper.class);
	@Override
	public OeeShiftMstInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		OeeShiftMstInfo oe = new OeeShiftMstInfo();
		try {
			oe.setShiftTypeCode(row.getString("SHIFT_MST_TYPE_CODE"));
			oe.setShiftTypeDesc(row.getString("SHIFT_MST_TYPE_DESCRIPTION"));
			
		}catch(Exception ex) {
			logger.error("OeeShiftMstRowMapper map row exception -->"+ex);
		}
		return oe;
	}

}
