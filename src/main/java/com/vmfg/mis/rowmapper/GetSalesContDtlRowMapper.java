package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.GetSalesContDtlEntity;

public class GetSalesContDtlRowMapper implements RowMapper<GetSalesContDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetSalesContDtlRowMapper.class);

	@Override
	public GetSalesContDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetSalesContDtlEntity ph = new GetSalesContDtlEntity();
		try {
		//	ph.setCustomerName(row.getString("CUSTOMER_NAME"));
			ph.setFinalCost(row.getString("FINAL_SALE"));
			ph.setSaleValue(row.getString("VAL"));
			ph.setTotalBaseCode(row.getString("TOTAL_BUDGET_COST"));
			
			
		} catch (Exception e) {
			logger.error("GetSalesContDtlRowMapper Exception--->" + e);
		}
		return ph;
	}

}
