package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class TimeLossReportRowMapper implements RowMapper<TimeLossReportInfo>{
	private static final Logger logger = LoggerFactory.getLogger(TimeLossReportRowMapper.class);
	@Override
	public TimeLossReportInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		TimeLossReportInfo tl = new TimeLossReportInfo();
		try {
			tl.setOperationCode(row.getString("OPERATION_ID"));
			tl.setProductCode(row.getString("PRODUCT_CODE"));
			tl.setReasonCode(row.getString("TIME_LOSS_MST_CODE"));
			tl.setTimeLossDtlid(row.getString("TIME_LOSS_DTL_ID"));
			tl.setTimeLossDuration(row.getString("TIME_LOSS_DURATION"));
		}catch(Exception ex) {
			logger.error("TimeLossReportRowMapper map row exception-->"+ex);
		}
		return tl;
	}

}
