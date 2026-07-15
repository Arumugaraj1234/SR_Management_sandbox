package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class DocumentPropertyMstRowMapper implements RowMapper<DocumentPropertyMst>{
	private static final Logger logger = LoggerFactory.getLogger(DocumentPropertyMstRowMapper.class);
	@Override
	public DocumentPropertyMst mapRow(ResultSet row, int rowNum) throws SQLException {
		DocumentPropertyMst tm = new DocumentPropertyMst();
		try {
			tm.setApproverDesignation(row.getString("APPROVING_DESIGNATION_CODE"));
			tm.setApprovingDepartment(row.getString("APPROVING_DEPT_CODE"));
			tm.setDocumentStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			tm.setSequence(row.getString("SEQUENCE"));
			tm.setStatusTypeCode(row.getString("DOCUMENT_STATUS_TYPE_CODE"));
			tm.setNotificationReq(row.getString("IS_NOTIFICATION_REQ"));
			tm.setDocLifeCycMstId(row.getString("DOCUMENT_LIFECYCLE_MST_ID"));
		} catch (Exception e) {
			logger.error("DocumentPropertyMstRowMapper Exception--->"+e);
		}
		return tm;
	}

}
