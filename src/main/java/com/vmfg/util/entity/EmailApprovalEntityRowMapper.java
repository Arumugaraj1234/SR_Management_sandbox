package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class EmailApprovalEntityRowMapper implements RowMapper<EmailApprovalEntity>{
	private static final Logger logger = LoggerFactory.getLogger(EmailApprovalEntityRowMapper.class);
	@Override
	public EmailApprovalEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		EmailApprovalEntity tm = new EmailApprovalEntity();
		try {
			tm.setIssuedbyname(row.getString("ISSUED_BYname"));
			tm.setApprovedbyname(row.getString("APPROVED_BYname"));
			tm.setApprovedby(row.getString("APPROVED_BY"));
			tm.setIssuedby(row.getString("ISSUED_BY"));
			tm.setCheckedbyname(row.getString("CHECKED_BYname"));
			tm.setCheckedby(row.getString("CHECKED_BY"));
			
			} catch (Exception e) {
			logger.error("EmailApprovalEntityRowMapper Exception--->"+e);
		}
		return tm;
	}

}
