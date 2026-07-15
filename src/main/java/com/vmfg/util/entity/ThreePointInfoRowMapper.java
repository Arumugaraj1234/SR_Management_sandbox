package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ThreePointInfoRowMapper implements RowMapper<ThreePointInfo>{
	private static final Logger logger = LoggerFactory.getLogger(ThreePointInfoRowMapper.class);
	@Override
	public ThreePointInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		ThreePointInfo tm = new ThreePointInfo();
		try {
			tm.setLineDesc(row.getString("LINE_MST_DESCRIPTION"));
			tm.setProgramDesc(row.getString("PROGRAM_DESCRIPTION"));
			tm.setProductCode(row.getString("PRODUCT_DESCRIPTION"));
			
			} catch (Exception e) {
			logger.error("ThreePointInfoRowMapper Exception--->"+e);
		}
		return tm;
	}

}
