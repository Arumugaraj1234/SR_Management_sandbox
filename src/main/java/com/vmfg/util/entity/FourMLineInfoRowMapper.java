package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.util.CommonMethod;

public class FourMLineInfoRowMapper implements RowMapper<LineProgramInfo>{
	private static final Logger logger = LoggerFactory.getLogger(FourMLineInfoRowMapper.class);
	@Override
	public LineProgramInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		LineProgramInfo tm = new LineProgramInfo();
		try {
			tm.setLineDesc(row.getString("LINE_MST_DESCRIPTION"));
			tm.setNameDesc(row.getString("CHANGE_AREA"));
			tm.setShift(row.getString("SHIFT_MST_TYPE_DESCRIPTION"));
			tm.setRefDate(CommonMethod.getDBDateToViewByMonthThree(row.getString("DATE")));
			} catch (Exception e) {
			logger.error("FourMLineInfoRowMapper Exception--->"+e);
		}
		return tm;
	}

}
