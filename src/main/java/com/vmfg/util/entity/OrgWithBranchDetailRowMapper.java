package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OrgWithBranchDetailRowMapper implements RowMapper<OrgWithBranchDetail>{
	private static final Logger logger = LoggerFactory.getLogger(OrgWithBranchDetailRowMapper.class);
	@Override
	public OrgWithBranchDetail mapRow(ResultSet row, int rowNum) throws SQLException {
		OrgWithBranchDetail ob = new OrgWithBranchDetail();
		try {
			
			ob.setOrganizationName(row.getString("ORG_NAME"));
			ob.setOrganizationCode(row.getString("ORG_CODE"));
			ob.setLogoPath(row.getString("LOGO_PATH"));
			ob.setBranchCode(row.getString("BRANCH_CODE"));
			ob.setBranchName(row.getString("BRANCH_NAME"));
			ob.setAddress1(row.getString("LOCATION_ADDRESSLINE1"));
			ob.setAddress2(row.getString("LOCATION_ADDRESSLINE2"));
			ob.setAddress3(row.getString("LOCATION_ADDRESSLINE3"));
			ob.setCountry(row.getString("COUNTRY_NAME"));
			ob.setState(row.getString("STATE_NAME"));
			ob.setCity(row.getString("LOCATION_CITY"));
			ob.setLocationReference("LOCATION_REFERENCENAME");
			
		} catch (Exception e) {
			logger.error("OrgWithBranchDetailRowMapper RowMapper Exception------>"+e);
		}
		return ob;
	}
}