package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.DebitNoteDtlListEntity;

public class DebitNoteDtlRowMapper implements RowMapper<DebitNoteDtlListEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DebitNoteDtlRowMapper.class);
	@Override
	public DebitNoteDtlListEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DebitNoteDtlListEntity row = new DebitNoteDtlListEntity();
		try {
			if (columnExists(rs, "PRODUCT_CODE")) {
				row.setProductCode(rs.getString("PRODUCT_CODE"));
			}
			if (columnExists(rs, "QTY")) {
				row.setQty(rs.getString("QTY"));
			}
			if (columnExists(rs, "INDENT_DTL_ID")) {
				row.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			}
			if (columnExists(rs, "PO_DTL_ID")) {
				row.setPoDtlId(rs.getString("PO_DTL_ID"));
			}
			
		} catch (Exception e) {
			logger.error("Exception in DebitNoteDtlRowMapper" + e);

		}

		return row;
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
