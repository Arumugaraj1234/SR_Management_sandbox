package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ShiftMasterListRowMapper implements RowMapper<ShiftMaster>{

	@Override
	public ShiftMaster mapRow(ResultSet row, int rowNum) throws SQLException {
		ShiftMaster sm = new ShiftMaster();
		try {
			sm.setShiftMstTypeCode(row.getString("SHIFT_MST_TYPE_CODE"));
		}catch(Exception ec) {
			
		}
		return sm;
	}

}
