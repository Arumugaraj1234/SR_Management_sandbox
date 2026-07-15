package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class WOGroupRowMapper implements RowMapper<WOGroupEntity>{
	private static final Logger logger = LoggerFactory.getLogger(WOGroupRowMapper.class);
	@Override
	public WOGroupEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		WOGroupEntity wo = new WOGroupEntity();
		try {
			wo.setProdCode(row.getString(""));
			wo.setProdDes(row.getString(""));
			wo.setRouteId(row.getString(""));
			wo.setSequence(row.getString(""));
			wo.setWoId(row.getString(""));
			wo.setWoPlStartDate(row.getString(""));
		}catch(Exception ex) {
			logger.error("WOGroupRowMapper Method Exception --->"+ex);
		}
		return wo;
	}

}
