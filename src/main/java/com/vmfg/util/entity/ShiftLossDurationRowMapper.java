package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ShiftLossDurationRowMapper implements RowMapper<ShiftLossDurationList>{

	@Override
	public ShiftLossDurationList mapRow(ResultSet row, int rowNum) throws SQLException {
		ShiftLossDurationList sl = new ShiftLossDurationList();
		try{
			sl.setAvailablityLoss(row.getString("TOTAL_AVAIL_LOSS"));
			sl.setPerformanceLoss(row.getString("TOTAL_PERFORM_LOSS"));
			sl.setTotalDuration(row.getString("TOTAL_LOSS_DURATION"));
		}catch(Exception ex) {
			
		}
		return sl;
	}

}
