package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OperationObservationEntryRowMapper implements RowMapper<OperationCalibrationEntryHdr> {
	private static final Logger logger = LoggerFactory.getLogger(OperationObservationEntryRowMapper.class);
	@Override
	public OperationCalibrationEntryHdr mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		OperationCalibrationEntryHdr hdr=new OperationCalibrationEntryHdr();
		try {

			hdr.setVerifiedbyname(rs.getString("VERIFIED_BYname"));
			hdr.setApprovedbyname(rs.getString("APPROVED_BYname"));
		
			hdr.setVerifiedby(rs.getString("VERIFIED_BY"));
			hdr.setApprovedby(rs.getString("APPROVED_BY"));
		
		
		}catch (Exception e) {
			logger.error("OperationObservationEntryRowMapper RowMapper Error"+e);
		}
		return hdr;
	}

}
