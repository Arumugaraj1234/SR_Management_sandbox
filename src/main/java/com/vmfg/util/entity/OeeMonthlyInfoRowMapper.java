package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OeeMonthlyInfoRowMapper implements RowMapper<OeeMonthlyInfo>{
	private static final Logger logger = LoggerFactory.getLogger(OeeMonthlyInfoRowMapper.class);
	@Override
	public OeeMonthlyInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		OeeMonthlyInfo oe = new OeeMonthlyInfo();
		try {
			oe.setActualQuantity(row.getString("PLAN_QUANTITY"));
			oe.setAvailablity(row.getString("AVAILABILITY"));
			oe.setBdTime(row.getBigDecimal("BD_TIME"));
			oe.setGapQUantity(row.getString("GAP_QUANTITY"));
			oe.setGapTime(row.getString("GAP_TIME"));
			oe.setJphActual(row.getString("JPH_ACTUAL"));
			oe.setJphPlan(row.getString("JPH_PLAN"));
			oe.setJpmhActual(row.getString("JPMH_ACTUAL"));
			oe.setJpmhPlan(row.getString("JPMH_PLAN"));
			oe.setNgQuantity(row.getString("NG_QUANTITY"));
			oe.setOee(row.getString("OEE"));
			oe.setPerformance(row.getString("PERFORMANCE"));
			oe.setPerformanceTime(row.getString("PERFORMANCE_TIME"));
			oe.setPlanQuantity(row.getString("PLAN_QUANTITY"));
			oe.setQuality(row.getString("QUALITY"));
			oe.setReworkQuantity(row.getString("REWORK_QTY"));
			oe.setPlannedProductionTime(row.getString("PLANNED_PRODCTION_TIME"));
		}catch(Exception ex) {
			logger.error("OeeMonthlyInfoRowMapper map row exception -->"+ex);
		}
		return oe;
	}

}
