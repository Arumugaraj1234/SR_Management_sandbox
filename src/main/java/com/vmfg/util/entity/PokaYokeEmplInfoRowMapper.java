package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class PokaYokeEmplInfoRowMapper implements RowMapper<PokaYokeEmplInfo>{
	private static final Logger logger = LoggerFactory.getLogger(PokaYokeEmplInfoRowMapper.class);
	@Override
	public PokaYokeEmplInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		PokaYokeEmplInfo qa = new PokaYokeEmplInfo();
		try {
			qa.setAuditorBy(row.getString("AUDITOR"));
			qa.setAuditorName(row.getString("AUDITOR_NAME"));
		
			qa.setAuditeeBy(row.getString("AUDITEE"));
			qa.setAuditeeName(row.getString("AUDITEE_NAME"));
			
			qa.setQaVerifiedBy(row.getString("QA_VERIFIED_BY"));
			qa.setQaVerifiedName(row.getString("QA_VERIFIED_NAME"));
			
			qa.setQaApprovedBy(row.getString("QA_APPROVED_BY"));
			qa.setQaApprovedName(row.getString("QA_APPROVED_NAME"));
			
			qa.setQaProductionApprovedBy(row.getString("PRODUCTION_APPROVED_BY"));
			qa.setQaProductionApprovedName(row.getString("PRODUCTION_APPROVED_NAME"));
		
		}catch(Exception ex) {
			logger.error("PokaYokeEmplInfoRowMapper map row exception -->"+ex);
		}
		return qa;
	}
}
