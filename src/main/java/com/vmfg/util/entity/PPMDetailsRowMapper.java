package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class PPMDetailsRowMapper implements RowMapper<PPMDetails>{
	private static final Logger logger = LoggerFactory.getLogger(PPMDetailsRowMapper.class);
	@Override
	public PPMDetails mapRow(ResultSet row, int rowNum) throws SQLException {
		PPMDetails pd = new PPMDetails();
		try {
			
			pd.setProducedQty(row.getString("PRODUCED_QUANTITY"));
			pd.setRejectedQty(row.getString("REJECTED_QUANTITY"));
			pd.setRpmId(row.getString("REPORT_PPM_ID"));
		} catch (Exception e) {
			logger.error("PPMDetailsRowMapper RowMapper Exception------>"+e);
		}
		return pd;
	}
}