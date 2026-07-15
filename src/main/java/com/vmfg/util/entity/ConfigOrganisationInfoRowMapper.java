package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ConfigOrganisationInfoRowMapper implements RowMapper< ConfigOrganisationInfoEntity>{
	private static final Logger logger = LoggerFactory.getLogger(ConfigOrganisationInfoRowMapper.class);
	@Override
	public ConfigOrganisationInfoEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		ConfigOrganisationInfoEntity ar = new ConfigOrganisationInfoEntity();
		try {
			
		ar.setIsInventoryEnabled(row.getString("IS_INVENTORY_ENABLED"));
		ar.setIsWMSEnabled(row.getString("IS_WMS_ENABLED"));
			
		} catch (Exception e) {
			logger.error("OrgWithBranchDetailRowMapper RowMapper Exception------>"+e);
		}
		return ar;
	}
}
