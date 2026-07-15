package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class DocLifeCycleListRowMapper implements RowMapper<DocLifeCycleListEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocLifeCycleListRowMapper.class);

	@Override
	public DocLifeCycleListEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DocLifeCycleListEntity result=new DocLifeCycleListEntity();
		try {
			result.setApprDesiCode(rs.getString("APPR_DESI"));
			result.setCancelSeq(rs.getString("CANCEL_SEQ"));
			result.setCurSeq(rs.getInt("CURR_SEQUENCE"));
			result.setDocStatus(rs.getString("DOC_STATUS"));
			result.setDocStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			result.setDocType(rs.getString("DOC_TYPE"));
			result.setDocTypeDesc(rs.getString("DOCUMENT_TYPE_DESCRIPTION"));
			result.setDsmId(rs.getString("DSM_ID"));
			result.setIsEditable(rs.getInt("IS_EDITABLE"));
			result.setLastSeq(rs.getInt("LAST_SEQ"));
			result.setNextSeq(rs.getString("NEXT_SEQ"));
			result.setProcessCode(rs.getString("PROCESS_CODE"));
			result.setSeqBatch(rs.getString("SEQ_BATCH"));
			result.setTenantId(rs.getString("TENANT_ID"));
			result.setIsActive(rs.getInt("IS_ACTIVE"));
			result.setSNo(rs.getInt("serial_number"));
		}catch(Exception ex) {
			logger.error("DocLifeCycleListRowMapper error "+ex);
		}
		return result;
	}

}
