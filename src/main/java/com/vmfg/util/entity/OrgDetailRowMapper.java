package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OrgDetailRowMapper implements RowMapper<OrgDetail>{
	private static final Logger logger = LoggerFactory.getLogger(OrgDetailRowMapper.class);
	@Override
	public OrgDetail mapRow(ResultSet row, int rowNum) throws SQLException {
		OrgDetail od = new OrgDetail();
		try {
			od.setOrganaizationName(row.getString("ORG_NAME"));
			od.setOrganizationCode(row.getString("ORG_CODE"));
			od.setLogoPath(row.getString("LOGO_PATH"));
		} catch (Exception e) {
			logger.error("OrgDetailRowMapper RowMapper Exception------>"+e);
		}
		return od;
	}

}

