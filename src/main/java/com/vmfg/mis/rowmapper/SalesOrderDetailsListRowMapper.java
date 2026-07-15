package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.SalesOrderDetailsList;

public class SalesOrderDetailsListRowMapper implements RowMapper<SalesOrderDetailsList> {
	private static final Logger logger = LoggerFactory.getLogger(SalesOrderDetailsListRowMapper.class);

	@Override
	public SalesOrderDetailsList mapRow(ResultSet row, int rowNum) throws SQLException {
		SalesOrderDetailsList ph = new SalesOrderDetailsList();
		try {
			ph.setMonthYr(row.getString("MONTH_YEAR"));
			ph.setVal(row.getString("VAL"));
			ph.setSeCount(row.getString("SE_COUNT"));
			
		} catch (Exception e) {
			logger.error("SalesOrderDetailsListRowMapper Exception--->" + e);
		}
		return ph;
	}


}
