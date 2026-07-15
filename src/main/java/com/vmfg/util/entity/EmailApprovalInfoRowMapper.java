package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class EmailApprovalInfoRowMapper implements RowMapper<EmailApprovalInfo>{
	private static final Logger logger = LoggerFactory.getLogger(EmailApprovalInfoRowMapper.class);
	@Override
	public EmailApprovalInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		EmailApprovalInfo tm = new EmailApprovalInfo();
		try {
			tm.setDocumentStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			tm.setSequence(row.getString("SEQUENCE"));
			tm.setDocMstId(row.getString("DOCUMENT_LIFECYCLE_MST_ID"));
			} catch (Exception e) {
			logger.error("EmailApprovalInfoRowMapper Exception--->"+e);
		}
		return tm;
	}

}
