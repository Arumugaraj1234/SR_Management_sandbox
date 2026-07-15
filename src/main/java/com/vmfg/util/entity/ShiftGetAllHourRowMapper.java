package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ShiftGetAllHourRowMapper implements RowMapper<ShiftTotalHourDetails>{
	private static final Logger logger = LoggerFactory.getLogger(ShiftGetAllHourRowMapper.class);

	@Override
	public ShiftTotalHourDetails mapRow(ResultSet row, int rowNum) throws SQLException {
		ShiftTotalHourDetails sh = new ShiftTotalHourDetails();
		try {
			sh.setHourEndTime(row.getString("END_TIME"));
			sh.setHourStartTime(row.getString("START_TIME"));
			sh.setIsNextDay(row.getString("IS_NEXT_DAY"));
			sh.setIsStartEndNextDay(row.getString("IS_START_END_NEXTDAY"));
			sh.setShfitMstHdrId(row.getString("SHIFT_MST_HDR_ID"));
			sh.setShiftConfigMstId(row.getString("SHIFT_MST_CONFIG_ID"));
			sh.setShiftEndDateTime(row.getString("STOP_TIME"));
			sh.setShiftEndTime(row.getString("SHIFT_END_TIME"));
			sh.setShiftHour(row.getString("HOUR"));
			sh.setShiftMstTypeCode(row.getString("SHIFT_MST_TYPE_CODE"));
			sh.setShiftStartDateTime(row.getString("S_START_TIME"));
			sh.setShiftStartTime(row.getString("SHIFT_START_TIME"));
			sh.setProductionTime(row.getString("PRODUCTION_TIME"));
			sh.setAvailTime(row.getString("AVAILABLE_TIME"));
		}catch(Exception ex) {
			logger.error("ShiftGetAllHourRowMapper Row Mapper Exception :"+ex);
		}
		return sh;
	}

}
