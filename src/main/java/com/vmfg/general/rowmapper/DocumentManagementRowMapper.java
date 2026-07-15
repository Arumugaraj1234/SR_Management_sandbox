package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.DocumentManagementEntity;

public class DocumentManagementRowMapper implements RowMapper<DocumentManagementEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocumentManagementRowMapper.class);

	@Override
	public DocumentManagementEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DocumentManagementEntity documentManagementEntity = new DocumentManagementEntity();
		try {
			if (columnExists(rs, "DOCUMENT_NAME")) {
				documentManagementEntity.setDocumentName(rs.getString("DOCUMENT_NAME"));
			}

			if (columnExists(rs, "VERSION")) {
				documentManagementEntity.setVersion(rs.getString("VERSION"));
			}
			if (columnExists(rs, "FILE_CREATED_BY")) {
				documentManagementEntity.setCreatedBy(rs.getString("FILE_CREATED_BY"));
			}
			if (columnExists(rs, "DM_ID")) {
				documentManagementEntity.setDmId(rs.getString("DM_ID"));
			}
			if (columnExists(rs, "DOCUMENT_TYPE_CODE")) {
				documentManagementEntity.setDocumentTypeCode(rs.getString("DOCUMENT_TYPE_CODE"));
			}
			if (columnExists(rs, "DESCRIPTION")) {
				documentManagementEntity.setDocumentTypeDescription(rs.getString("DESCRIPTION"));
			}
			if (columnExists(rs, "STG_DESC")) {
				documentManagementEntity.setStgDescription(rs.getString("STG_DESC"));
			}
			if (columnExists(rs, "STG_CODE")) {
				documentManagementEntity.setStdCode(rs.getString("STG_CODE"));
			}
			if (columnExists(rs, "FILE_CREATED_DATE")) {
				documentManagementEntity.setFileCreatedDate(rs.getString("FILE_CREATED_DATE"));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				documentManagementEntity.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}

			if (columnExists(rs, "FU_CODE")) {
				documentManagementEntity.setFuCode(rs.getString("FU_CODE"));
			}

			if (columnExists(rs, "FILE_ABSOLUTE_NAME")) {
				documentManagementEntity.setFileName(rs.getString("FILE_ABSOLUTE_NAME"));
			}
			if (columnExists(rs, "REMARKS")) {
				documentManagementEntity.setRemarks(rs.getString("REMARKS"));
			}
			if (columnExists(rs, "FILE_NAME_EXTN")) {
				documentManagementEntity.setFileNameExtn(rs.getString("FILE_NAME_EXTN"));
			}
			if (columnExists(rs, "IS_PDF")) {
				documentManagementEntity.setIsPdf(rs.getString("IS_PDF"));
			}

		} catch (Exception ex) {
			logger.error("DocumentManagementRowMapper  Method Exception" + ex);

		}
		return documentManagementEntity;
	}

	// column checking purpose (column is there or not)
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
