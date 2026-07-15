package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.DocumentStatusMstEntity;

public class DocumentStatusMstRowMapper  implements RowMapper<DocumentStatusMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocumentStatusMstRowMapper.class);

	@Override
	public DocumentStatusMstEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		DocumentStatusMstEntity tm = new DocumentStatusMstEntity();
		try {
			tm.setApprDept(row.getString("APPR_DEPT"));
			tm.setApprDesi(row.getString("APPR_DESI"));
			tm.setCancelSeq(row.getString("CANCEL_SEQ"));
			tm.setCurrSequence(row.getString("CURR_SEQUENCE"));
			tm.setDocStatus(row.getString("DOC_STATUS"));
			tm.setDocStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			tm.setDocType(row.getString("DOC_TYPE"));
			tm.setDocTypeDesc(row.getString("DOCUMENT_TYPE_DESCRIPTION"));
			tm.setDsmId(row.getString("DSM_ID"));
			tm.setIsNotify(row.getString("IS_NOTIFY"));
			tm.setNextSeq(row.getString("NEXT_SEQ"));
			tm.setSeqBatch(row.getString("SEQ_BATCH"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setIsActive(row.getString("IS_ACTIVE"));
			tm.setLastSeq(row.getString("LAST_SEQ"));
			tm.setIsEditable(row.getString("IS_EDITABLE"));
			tm.setProcessCode(row.getString("PROCESS_CODE"));
		} catch (Exception e) {
			logger.error("DocumentStatusMstRowMapper Exception--->" + e);
		}
		return tm;
	}

}
