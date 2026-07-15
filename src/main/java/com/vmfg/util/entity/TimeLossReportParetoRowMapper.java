package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class TimeLossReportParetoRowMapper  implements RowMapper<TimeLossReportPareto>{
	private static final Logger logger = LoggerFactory.getLogger(TimeLossReportParetoRowMapper.class);
	@Override
	public TimeLossReportPareto mapRow(ResultSet row, int rowNum) throws SQLException {
		TimeLossReportPareto tm = new TimeLossReportPareto();
		try {
			tm.setReasonCode(row.getString("REASON_CODE"));
			tm.setReasonDesc(row.getString("REASON_DESCRIPTION"));
			tm.setTimeLossDuration(row.getString("TIME_LOSS_DURATION"));
			tm.setTimeLossCount(row.getString("TIME_LOSS_COUNT"));
			
			} catch (Exception e) {
			logger.error("TimeLossReportPareto Exception--->"+e);
		}
		return tm;
	}

}
