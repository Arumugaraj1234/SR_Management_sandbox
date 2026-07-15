package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class TransactionMstDtlsRowMapper implements RowMapper<TransactionMstDtls>{
	private static final Logger logger = LoggerFactory.getLogger(TransactionMstDtlsRowMapper.class);
	@Override
	public TransactionMstDtls mapRow(ResultSet row, int rowNum) throws SQLException {
		TransactionMstDtls td = new TransactionMstDtls();
		try {
			
			td.setIdStart(row.getString("ID_START"));
			if(row.getString("PREFIX_IDENTIFIER")!=null) {
				td.setPrefix(row.getString("PREFIX_IDENTIFIER"));
			}else {
				td.setPrefix("");
			}
			
			if(row.getString("SUFFIX_IDENTIFIER")!=null) {
				td.setSuffix(row.getString("SUFFIX_IDENTIFIER"));
			}else {
				td.setSuffix("");
			}
			
			
		} catch (Exception e) {
			logger.error("TransactionMstDtlsRowMapper Exception--->"+e);
		}
		return td;
	}

}
