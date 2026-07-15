package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ScheduleConfigInfoRowMapper implements RowMapper<ScheduleConfigInfo>{
	private static final Logger logger = LoggerFactory.getLogger(ScheduleConfigInfoRowMapper.class);
	@Override
	public ScheduleConfigInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		ScheduleConfigInfo qa = new ScheduleConfigInfo();
		try {
			qa.setVerifiedbyname(row.getString("VERIFIED_BYname"));
			qa.setApprovedbyname(row.getString("APPROVED_BYname"));
			qa.setPreparedbyname(row.getString("PREPARED_BYname"));
		
			qa.setVerifiedby(row.getString("VERIFIED_BY"));
			qa.setApprovedby(row.getString("APPROVED_BY"));
			qa.setPreparedby(row.getString("PREPARED_BY"));
		
		}catch(Exception ex) {
			logger.error("ScheduleConfigInfoRowMapper map row exception -->"+ex);
		}
		return qa;
	}

}
