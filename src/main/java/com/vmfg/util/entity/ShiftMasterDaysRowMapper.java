package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ShiftMasterDaysRowMapper implements RowMapper<ShiftMaster>{
	private static final Logger logger = LoggerFactory.getLogger(ShiftMasterDaysRowMapper.class);
	@Override
	public ShiftMaster mapRow(ResultSet row, int rowNum) throws SQLException {
		ShiftMaster sh = new ShiftMaster();
		try {
			sh.setShiftMasterHdrId(row.getString("SHIFT_MST_HDR_ID"));
			sh.setShiftDays(row.getString("SHIFT_DAY"));
			sh.setShiftMstTypeCode(row.getString("SHIFT_MST_TYPE_CODE"));
			sh.setShiftMstTypeDesc(row.getString("SHIFT_MST_TYPE_DESCRIPTION"));
		}catch(Exception ex) {
			logger.info("ShiftMasterRowMapper map row exception -->"+ex);
		}
		return sh;
	}

}
