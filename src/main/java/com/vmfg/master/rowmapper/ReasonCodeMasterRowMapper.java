package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.ReasonCodeMasterEntity;

public class ReasonCodeMasterRowMapper implements RowMapper<ReasonCodeMasterEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ReasonCodeMasterRowMapper.class);
	@Override
	public ReasonCodeMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReasonCodeMasterEntity resp = new ReasonCodeMasterEntity();
		try {
			if (columnExists(rs, "REASON_CODE")) {
				resp.setReasonCode(rs.getString("REASON_CODE"));
			}
			if (columnExists(rs, "REASON_DESCRIPTION")) {
				resp.setReasonCodeDesc(rs.getString("REASON_DESCRIPTION"));
			}
			if (columnExists(rs, "REASON_CODE_TYPE")) {
				resp.setReasonType(rs.getString("REASON_CODE_TYPE"));
			}
			if (columnExists(rs, "REASON_TYPE_DESCRIPTION")) {
				resp.setReasonTypeDesc(rs.getString("REASON_TYPE_DESCRIPTION"));
			}
		}catch(Exception ex) {
			logger.error("ReasonCodeMasterRowMapper  Method Exception" + ex);
		}
		return resp;
		
	}

	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
				return true;
			}
		}

		return false;
	}
	
	
}
