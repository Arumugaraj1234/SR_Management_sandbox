package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.ScmHdrEntity;

public class ScmHdrRowMapper implements RowMapper<ScmHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ScmHdrRowMapper.class);

	@Override
	public ScmHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ScmHdrEntity scmHdrEntity = new ScmHdrEntity();
		try {
			if (columnExists(rs, "PM_HDR_ID")) {
				scmHdrEntity.setPmHdrId(rs.getString("PM_HDR_ID"));
			}
			if (columnExists(rs, "SCM_HDR_ID")) {
				scmHdrEntity.setScmHdrId(rs.getString("SCM_HDR_ID"));
			}
			if (columnExists(rs, "SCM_INITIATED_DATE")) {
				scmHdrEntity.setScmInitiatedDate(rs.getString("SCM_INITIATED_DATE"));
			}
			if (columnExists(rs, "serial_number")) {
				scmHdrEntity.setSNo(rs.getString("serial_number"));
			}
			if (columnExists(rs, "DUE_DATE")) {
				scmHdrEntity.setDueDate(rs.getString("DUE_DATE"));
			}
			if (columnExists(rs, "TRANSACTION_STATUS")) {
				scmHdrEntity.setTransactionStatus(rs.getString("TRANSACTION_STATUS"));
			}
			if (columnExists(rs, "TRANSACTION_STATUS_SEQ")) {
				scmHdrEntity.setTransactionStatusSeq(rs.getString("TRANSACTION_STATUS_SEQ"));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				scmHdrEntity.setHdrStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			}
			if (columnExists(rs, "IS_INTERNAL")) {
				scmHdrEntity.setIsInternal(rs.getString("IS_INTERNAL"));
			}

			scmHdrEntity.setEnquiryId(rs.getString("ENQUIRY_ID"));
		} catch (Exception ex) {
			logger.error("ScmHdrBasedDtlRowMapper  Method Exception" + ex);

		}
		return scmHdrEntity;
	}

	// column checking purpose (column is there or not)
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
