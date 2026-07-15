package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.ApprovedDocEntity;

public class ApprovedDocRowMapper implements RowMapper<ApprovedDocEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocumentAppStatusDtlRowMapper.class);

	@Override
	public ApprovedDocEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		ApprovedDocEntity tm = new ApprovedDocEntity();
		try {
			tm.setDmId(row.getString("DM_ID"));
			tm.setDocument(row.getString("DOCUMENT_TYPE_CODE"));
			tm.setDocumentType(row.getString("DOCUMENT_TYPE_DESCRIPTION"));
			tm.setUploadDocType(row.getString("UPLOAD_DOC_TYPE"));
			tm.setUploadDocument(row.getString("UPLOAD_DOCUMENT"));
			tm.setVersion(row.getString("VERSION"));
			tm.setDocumentName(row.getString("DOCUMENT_NAME"));
			tm.setCreatedBy(row.getString("EMPLOYEE_FIRSTNAME"));
			tm.setRemarks(row.getString("REMARKS"));
			tm.setCreatedDate(row.getString("FILE_CREATED_DATE"));
			tm.setReferenceId(row.getString("REFERENCE_ID"));
			tm.setStageCode(row.getString("STAGE_CODE"));
			tm.setFilename(row.getString("FILE_ABSOLUTE_NAME"));
			tm.setIsPdf(row.getString("IS_PDF"));
		} catch (Exception e) {
			logger.error("approvedDocRowMapper Exception--->" + e);
		}
		return tm;
	}

}
