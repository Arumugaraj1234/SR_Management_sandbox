package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.BillingDetailEntity;

public class BillingDetailRowMapper implements RowMapper<BillingDetailEntity> {
	private static final Logger logger = LoggerFactory.getLogger(BillingDetailRowMapper.class);

	@Override
	public BillingDetailEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		BillingDetailEntity bd = new BillingDetailEntity();
		try {
			bd.setOrgCode(rs.getString("ORG_CODE"));
            bd.setOrgName(rs.getString("ORG_NAME"));
            bd.setLocationId(rs.getString("LOCATION_ID"));
            bd.setLocationRefName(rs.getString("LOCATION_REFERENCENAME"));
            bd.setLocAddressLine(rs.getString("LOCATION_ADDRESSLINE"));
            bd.setLocCity(rs.getString("LOCATION_CITY"));
            bd.setLocState(rs.getString("LOCATION_STATE"));
            bd.setLocCountryCode(rs.getString("LOCATION_COUNTRY_CODE"));
            bd.setLocPinCode(rs.getString("LOCATION_PINCODE"));
            bd.setContactNo(rs.getString("CONTACT_NO"));
            bd.setGstNo(rs.getString("GST_NUMBER"));
		} catch (Exception ex) {
			logger.error("BillingDetailRowMapper error " + ex);
		}
		return bd;
	}

}
