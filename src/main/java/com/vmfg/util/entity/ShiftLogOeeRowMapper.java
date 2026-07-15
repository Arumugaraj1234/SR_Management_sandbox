package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ShiftLogOeeRowMapper implements RowMapper<ShiftLogOeeInfo>{
	private static final Logger logger = LoggerFactory.getLogger(ShiftLogOeeRowMapper.class);
	@Override
	public ShiftLogOeeInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		ShiftLogOeeInfo si = new ShiftLogOeeInfo();
		try {
			si.setShift(row.getString("SHIFT"));
			si.setShiftDate(row.getString("SHIFT_DATE"));
			si.setShiftYearMonth(row.getString("SHIFT_YEAR_MONTH"));
			si.setLineCode(row.getString("LINE_CODE"));
			si.setShiftYear(row.getString("SHIFT_YEAR"));
			si.setShiftTypeCode(row.getString("SHIFT_MST_TYPE_CODE"));
			si.setShiftMonth(row.getString("SHIFT_MONTH"));
			si.setEquipmentID(row.getString("EQUIPMENT_ID"));
		}catch(Exception ex) {
			logger.error("ShiftLogOeeRowMapper map row exception -->"+ex);
		}
		return si;
	}

}
