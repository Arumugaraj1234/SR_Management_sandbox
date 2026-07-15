package com.vmfg.finance.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.finance.entity.PraDtlsHistoryEntity;

public class PraDtlsHistoryRowMapper implements RowMapper<PraDtlsHistoryEntity>{
	private static final Logger logger = LoggerFactory.getLogger(PraDtlsHistoryRowMapper.class);

	@Override
	public PraDtlsHistoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		PraDtlsHistoryEntity result=new PraDtlsHistoryEntity();
		
		try {
			result.setPraNum(rs.getString("PRA_CODE"));
			result.setPraStatusCode(rs.getString("STATUS"));
			result.setPraStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			result.setPraValue(rs.getString("INVOICE_VALUE"));
			
		}catch(Exception ex) {
			logger.error("PraDtlsHistoryRowMapper error "+ex);
		}
		return result;
	}

}
