package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class PerformanceReportEntityRowMapper implements RowMapper<PerformanceReportEntity>{
	private static final Logger logger = LoggerFactory.getLogger(PerformanceReportEntityRowMapper.class);
	@Override
	public PerformanceReportEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		PerformanceReportEntity pr = new PerformanceReportEntity();
		try {
			
			pr.setPlannedQty(row.getString("PLANNED_QUANTITY"));
			pr.setProducedQty(row.getString("PRODUCED_QUANTITY"));
			pr.setRpId(row.getString("REPORT_PERFORMANCE_ID"));
			
		} catch (Exception e) {
			logger.error("PerformanceReportEntityRowMapper RowMapper Exception------>"+e);
		}
		return pr;
	}
}