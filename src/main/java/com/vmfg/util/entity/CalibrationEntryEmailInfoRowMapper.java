package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class CalibrationEntryEmailInfoRowMapper implements RowMapper<CalibrationEntryMailHdr>{
	private static final Logger logger = LoggerFactory.getLogger(CalibrationEntryEmailInfoRowMapper.class);
	@Override
	public CalibrationEntryMailHdr mapRow(ResultSet rs, int rowNum) throws SQLException {
		CalibrationEntryMailHdr hdr=new CalibrationEntryMailHdr();
	
		try {

			hdr.setVerifiedbyname(rs.getString("VERIFIED_BYname"));
			hdr.setApprovedbyname(rs.getString("APPROVED_BYname"));
			hdr.setPreparedby(rs.getString("PREPARED_BY"));
			hdr.setPreparedbyname(rs.getString("PREPARED_BYname"));
			hdr.setVerifiedby(rs.getString("VERIFIED_BY"));
			hdr.setApprovedby(rs.getString("APPROVED_BY"));


		}catch (Exception e) {
			logger.error("CalibrationEntryEmailInfoRowMapper map row exception -->"+e);
			
		}
		return hdr;
	}

}
