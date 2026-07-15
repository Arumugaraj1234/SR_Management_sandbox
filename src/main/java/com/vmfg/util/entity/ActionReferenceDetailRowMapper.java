package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ActionReferenceDetailRowMapper implements RowMapper<ActionReferenceDetail>{
	private static final Logger logger = LoggerFactory.getLogger(ActionReferenceDetailRowMapper.class);
	@Override
	public ActionReferenceDetail mapRow(ResultSet row, int rowNum) throws SQLException {
		ActionReferenceDetail ar = new ActionReferenceDetail();
		try {
			
		ar.setReferenceCode(row.getString("REFERENCE_CODE"));
		ar.setReferenceId(row.getString("REFERENCE_ID"));
			
		} catch (Exception e) {
			logger.error("OrgWithBranchDetailRowMapper RowMapper Exception------>"+e);
		}
		return ar;
	}
}