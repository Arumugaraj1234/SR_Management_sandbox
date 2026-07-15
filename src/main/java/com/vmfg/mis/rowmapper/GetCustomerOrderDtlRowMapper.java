package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.GetCustomerOrderDtlEntity;

public class GetCustomerOrderDtlRowMapper implements RowMapper<GetCustomerOrderDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetCustomerOrderDtlRowMapper.class);

	@Override
	public GetCustomerOrderDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetCustomerOrderDtlEntity ph = new GetCustomerOrderDtlEntity();
		try {
		//	ph.setDescription(row.getString("DESCRIPTION"));
			ph.setCustomerName(row.getString("CUSTOMER_NAME"));
			ph.setNoOfOrder(row.getString("NO_OF_ORDERS"));
			ph.setTenantId(row.getString("TENANT_ID"));
			
		} catch (Exception e) {
			logger.error("GetCustomerOrderDtlRowMapper Exception--->" + e);
		}
		return ph;
	}

}
