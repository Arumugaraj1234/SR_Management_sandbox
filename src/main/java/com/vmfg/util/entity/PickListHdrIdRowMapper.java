package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class PickListHdrIdRowMapper implements RowMapper<PickListHdrIdentity> {
	private static final Logger logger = LoggerFactory.getLogger(PickListHdrIdRowMapper.class);
	@Override
	public PickListHdrIdentity mapRow(ResultSet row, int rowNum) throws SQLException {
		PickListHdrIdentity fi = new PickListHdrIdentity();
		try {
			fi.setPicklisthdrid(row.getInt("PL_HDR_ID"));
		}catch(Exception ex) {
			logger.error("PickListHdrIdRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
