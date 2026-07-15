package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.DocumentManagementTblEntity;

public class DocumentManagementTblRowMapper implements RowMapper<DocumentManagementTblEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocumentAppStatusDtlRowMapper.class);

	@Override
	public DocumentManagementTblEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		DocumentManagementTblEntity tm = new DocumentManagementTblEntity();
		try {
			tm.setDmId(row.getString("DM_ID"));
			tm.setStageCode(row.getString("STAGE_CODE"));
			tm.setUploadDocType(row.getString("UPLOAD_DOC_TYPE"));
			tm.setTenantdId(row.getString("TENANT_ID"));
			tm.setApproved(row.getString("APPROVED"));
			tm.setDocumentName(row.getString("DOCUMENT_NAME"));
			tm.setEnquiryId(row.getString("ENQUIRY_ID"));
			tm.setLatestVersion(row.getString("LATEST_VERSION"));
			tm.setProjectId(row.getString("PROJECT_ID"));
			tm.setRefId(row.getString("REFERENCE_ID"));
			tm.setVersion(row.getString("VERSION"));
			tm.setRemarks(row.getString("REMARKS"));
			tm.setDocApprSeq(row.getString("DOC_APPR_SEQ"));
		
		} catch (Exception e) {
			logger.error("DocumentManagementTblRowMapper Exception--->" + e);
		}
		return tm;
	}

}

