package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.CustomerMstEntity;

public class CustomerMasterDtlsRowMapper implements RowMapper<CustomerMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(CustomerMasterDtlsRowMapper.class);

	@Override
	public CustomerMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		CustomerMstEntity res = new CustomerMstEntity();
		try {
			res.setAddress(rs.getString("ADDRESS"));
			res.setCity(rs.getString("CITY"));
			res.setContactNumber(rs.getString("CONTACT_NO"));
			res.setCountry(rs.getString("COUNTRY"));
			res.setCustomerCode(rs.getString("CUST_CODE"));
			res.setCustomerName(rs.getString("CUST_NAME"));
			res.setGstNumber(rs.getString("GST"));
			res.setPanNumber(rs.getString("PAN"));
			res.setPincode(rs.getString("PINCODE"));
			res.setState(rs.getString("STATE"));
		} catch (Exception ex) {
			logger.error("CustomerMasterDtlsRowMapper  Method Exception" + ex);
		}
		return res;
	}
}
