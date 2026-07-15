package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;


public class ShiftLogHdrRowMapper implements RowMapper<ShiftLogHdr>{
	private static final Logger logger = LoggerFactory.getLogger(ShiftLogHdrRowMapper.class);
	@Override
	public ShiftLogHdr mapRow(ResultSet row, int rowNum) throws SQLException {
		ShiftLogHdr si = new ShiftLogHdr();
		try {
			si.setShiftLogHdrID(row.getInt("SHIFT_LOG_HDR_ID"));
		}catch(Exception ex) {
			logger.error("ShiftLogOeeRowMapper map row exception -->"+ex);
		}
		return si;
	}

}
