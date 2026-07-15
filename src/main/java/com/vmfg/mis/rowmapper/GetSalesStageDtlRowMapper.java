package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.GetSalesStageDtlEntity;

public class GetSalesStageDtlRowMapper implements RowMapper<GetSalesStageDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SalesOrderDetailsListRowMapper.class);

	@Override
	public GetSalesStageDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetSalesStageDtlEntity ph = new GetSalesStageDtlEntity();
		try {
			ph.setDescription(row.getString("DESCRIPTION"));
			ph.setVal(row.getString("VAL"));
			ph.setSeCount(row.getString("SE_COUNT"));
			
		} catch (Exception e) {
			logger.error("GetSalesStageDtlRowMapper Exception--->" + e);
		}
		return ph;
	}


}
