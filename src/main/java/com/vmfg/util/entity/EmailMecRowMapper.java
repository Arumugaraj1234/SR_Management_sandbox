package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class EmailMecRowMapper implements RowMapper<EmailMec>{
	private static final Logger logger = LoggerFactory.getLogger(EmailMecRowMapper.class);
	@Override
	public EmailMec mapRow(ResultSet row, int rowNum) throws SQLException {
		EmailMec em = new EmailMec();
		try {
			
			em.setMesDescription(row.getString("MEC_DESCRIPTION"));
			em.setMesShort(row.getString("MEC_SHORT_DESCRIPTION"));
			
		} catch (Exception e) {
			logger.error("FinancialYearMstRowMapper Exception--->"+e);
		}
		return em;
	}

}
