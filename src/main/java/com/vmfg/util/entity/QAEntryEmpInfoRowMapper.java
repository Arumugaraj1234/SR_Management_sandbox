package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class QAEntryEmpInfoRowMapper implements RowMapper<QAEntryEmplInfo>{
	private static final Logger logger = LoggerFactory.getLogger(QAEntryEmpInfoRowMapper.class);
	@Override
	public QAEntryEmplInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		QAEntryEmplInfo qa = new QAEntryEmplInfo();
		try {
			qa.setVerifiedbyname(row.getString("VERIFIED_BYname"));
			qa.setApprovedbyname(row.getString("APPROVED_BYname"));
		
			qa.setVerifiedby(row.getString("VERIFIED_BY"));
			qa.setApprovedby(row.getString("APPROVED_BY"));
		
		}catch(Exception ex) {
			logger.error("QAEntryEmpInfoRowMapper map row exception -->"+ex);
		}
		return qa;
	}

}
