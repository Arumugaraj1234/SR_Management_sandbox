package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class DocLifeCycleMstLogRowMapper implements RowMapper<DocLifeCycleMstLogEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocLifeCycleMstLogRowMapper.class);

	@Override
	public DocLifeCycleMstLogEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DocLifeCycleMstLogEntity result=new DocLifeCycleMstLogEntity();
		try {
			result.setApprDesiCode(rs.getString("APPR_DESI"));
			result.setCancelSeq(rs.getString("CANCEL_SEQ"));
			result.setCurSeq(rs.getString("CURR_SEQUENCE"));
			result.setDocStatus(rs.getString("DOC_STATUS"));
			result.setDocStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			result.setDocType(rs.getString("DOC_TYPE"));
			result.setDocTypeDesc(rs.getString("DOCUMENT_TYPE_DESCRIPTION"));
			result.setDsmLogId(rs.getString("DSM_LOG_ID"));
			result.setIsEditable(rs.getString("IS_EDITABLE"));
			result.setLastSeq(rs.getString("LAST_SEQ"));
			result.setNextSeq(rs.getString("NEXT_SEQ"));
			result.setProcessCode(rs.getString("PROCESS_CODE"));
			result.setSeqBatch(rs.getString("SEQ_BATCH"));
			result.setDocGroup(rs.getString("DOC_GROUP"));
			result.setVersion(rs.getString("VERSION"));
			result.setVersionDate(rs.getString("VERSION_DATETIME"));
			result.setUpdatedBy(rs.getString("EMPLOYEE_FIRSTNAME"));
			result.setTenantId(rs.getString("TENANT_ID"));
		}catch(Exception ex) {
			logger.error("DocLifeCycleMstLogRowMapper error "+ex);
		}
		return result;
	}

} 
