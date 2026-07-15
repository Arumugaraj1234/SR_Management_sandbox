package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OEEGapEmplInfoRowMapper implements RowMapper<OEEGapEmplInfo>{
	private static final Logger logger = LoggerFactory.getLogger(OEEGapEmplInfoRowMapper.class);
	@Override
	public OEEGapEmplInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		OEEGapEmplInfo oe = new OEEGapEmplInfo();
		try {
			oe.setPreparedbyname(row.getString("PREPARED_BYname"));
			oe.setApprovedbyname(row.getString("APPROVED_BYname"));
		
			oe.setPreparedby(row.getString("PREPARED_BY"));
			oe.setApprovedby(row.getString("APPROVED_BY"));
		
		}catch(Exception ex) {
			logger.error("OEEGapEmplInfoRowMapper map row exception -->"+ex);
		}
		return oe;
	}

}
