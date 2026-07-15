package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class PPMDailyRowmapper implements RowMapper<PPMDailyInfo>{
	private static final Logger logger = LoggerFactory.getLogger(PPMDailyRowmapper.class);
	@Override
	public PPMDailyInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		PPMDailyInfo pr = new PPMDailyInfo();
		try {
			pr.setPpmdId(row.getString("REPORT_PPMD_ID"));
			pr.setProducedQuantity(row.getString("REPORT_PPMD_ID"));
			pr.setRejectedQuantity(row.getString("REPORT_PPMD_ID"));
		}catch(Exception ex) {
			logger.error("PPMDailyRowmapper map row exception -->"+ex);
		}
		return pr;
	}

}
