package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.CustomerMstEntity;

public class CustomerMstRowMapper implements RowMapper<CustomerMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(CustomerMstRowMapper.class);

	@Override
	public CustomerMstEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		CustomerMstEntity tm = new CustomerMstEntity();
		try {
			tm.setAddress(row.getString("ADDRESS"));
			tm.setCity(row.getString("CITY"));
			tm.setCountry(row.getString("COUNTRY"));
			tm.setCustCode(row.getString("CUST_CODE"));
			tm.setCustName(row.getString("CUST_NAME"));
			tm.setGst(row.getString("GST"));
			tm.setPan(row.getString("PAN"));
			tm.setPincode(row.getString("PINCODE"));
			tm.setState(row.getString("STATE"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setContactNo(row.getString("CONTACT_NO"));
		} catch (Exception e) {
			logger.error("CustomerMstRowMapper Exception--->" + e);
		}
		return tm;
	}

}
