package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.response.DocumentStatusTypeCode;

public class DocumentStatusTypeCodeRowMapper implements RowMapper<DocumentStatusTypeCode> {
	private static final Logger logger = LoggerFactory.getLogger(DocumentStatusTypeCodeRowMapper.class);

	@Override
	public DocumentStatusTypeCode mapRow(ResultSet rs, int rowNum) throws SQLException {
		DocumentStatusTypeCode res = new DocumentStatusTypeCode();
		try {
			res.setDocumentStatusTypeCode(rs.getString("DOCUMENT_STATUS_TYPE_CODE"));
			res.setDocumentStatusTypeDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
		} catch (Exception ex) {
			logger.error("DocumentStatusTypeCodeRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
