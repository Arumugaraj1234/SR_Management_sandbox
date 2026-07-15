package com.vmfg.finance.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.finance.entity.GrnDtlsEntity;

public class GrnDtlsRowMapper implements RowMapper<GrnDtlsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GrnDtlsRowMapper.class);

	@Override
	public GrnDtlsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		GrnDtlsEntity result =new GrnDtlsEntity();
		
		try {
			if (columnExists(rs, "GRN_DATE")) {
				result.setGrnDate(rs.getString("GRN_DATE"));
			}
			if(columnExists(rs, "GRN_CODE")){
			result.setGrnNo(rs.getString("GRN_CODE"));
			}
			if(columnExists(rs, "RECEIVED_QTY")){
			result.setGrnQty(rs.getString("RECEIVED_QTY"));
			}
			if(columnExists(rs, "INVOICE_DATE")){
			result.setInvoiceDate(rs.getString("INVOICE_DATE"));
			}
			if(columnExists(rs, "INVOICE_NO")){
			result.setInvoiceNo(rs.getString("INVOICE_NO"));
			}
		}catch(Exception ex) {
			logger.error("GrnDtlsRowMapper error "+ex);
		}
		return result;
	}

	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}

}
