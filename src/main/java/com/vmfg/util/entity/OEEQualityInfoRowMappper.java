package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OEEQualityInfoRowMappper implements RowMapper<OEEQualityinfo>{
	private static final Logger logger = LoggerFactory.getLogger(OEEQualityInfoRowMappper.class);
	@Override
	public OEEQualityinfo mapRow(ResultSet row, int rowNum) throws SQLException {
		OEEQualityinfo qi = new OEEQualityinfo();
		try {
			qi.setAvailability(row.getString("AVAILABILITY"));
			qi.setPerformance(row.getString("PERFORMANCE"));
			qi.setAvailabilityDecimal(row.getString("AVAILABILITY_DECIMAL"));
			qi.setPerformanceDecimal(row.getString("PERFORMANCE_DECIMAL"));
		}catch(Exception ex) {
			logger.error("OEEQualityInfoRowMappper map row exception :"+ex);
		}
		return qi;
	}

}
