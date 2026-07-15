package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class DocumentTypeRowMapper implements RowMapper<DocumentTypeEntity>{
	private static final Logger logger = LoggerFactory.getLogger(DocumentTypeRowMapper.class);

	@Override
	public DocumentTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DocumentTypeEntity result=new DocumentTypeEntity();
		try {
			result.setDocTypeCode(rs.getString("DOCUMENT_TYPE_CODE"));
			result.setDocTypeDesc(rs.getString("DOCUMENT_TYPE_DESCRIPTION"));

		}catch(Exception ex) {
			logger.error("DocumentTypeRowMapper error "+ex);
		}
		return result;
	}

}
