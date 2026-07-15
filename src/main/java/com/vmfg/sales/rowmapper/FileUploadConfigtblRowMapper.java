package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.FileUploadConfigtblEntity;

public class FileUploadConfigtblRowMapper implements RowMapper<FileUploadConfigtblEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocumentAppStatusDtlRowMapper.class);

	@Override
	public FileUploadConfigtblEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		FileUploadConfigtblEntity tm = new FileUploadConfigtblEntity();
		try {
			tm.setDescription(row.getString("DESCRIPTION"));
			tm.setDocumentTypeCode(row.getString("DOCUMENT_TYPE_CODE"));
			tm.setFuCode(row.getString("FU_CODE"));
			tm.setTenantId(row.getString("TENANT_ID"));
		
		} catch (Exception e) {
			logger.error("FileUploadConfigtblRowMapper Exception--->" + e);
		}
		return tm;
	}

}

