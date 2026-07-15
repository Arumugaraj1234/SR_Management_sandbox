package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.QIStatusEntity;

public class QIStatusRowMapper implements RowMapper<QIStatusEntity> {
	private static final Logger logger = LoggerFactory.getLogger(QIStatusRowMapper.class);

	@Override
	public QIStatusEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		QIStatusEntity qi = new QIStatusEntity();
		try {
				qi.setQsdId(rs.getString("QSD_ID"));
		        qi.setReferenceId(rs.getString("REFERENCE_ID"));
		        qi.setReferenceDoc(rs.getString("REFERENCE_DOC"));
		        qi.setSequenceNo(rs.getString("SEQUENCE_NO"));
		        qi.setSequenceStatus(rs.getString("SEQUENCE_STATUS"));
		        qi.setRemarks(rs.getString("REMARKS"));
		        qi.setUpdatedBy(rs.getString("UPDATED_BY"));
		        qi.setUpdatedOn(rs.getString("UPDATED_ON"));
		        qi.setTenantId(rs.getString("TENANT_ID"));
		        qi.setSeqStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
	        
		} catch (Exception e) {
			logger.error("QIStatusRowMapper Exception--->" + e);
		}
		return qi;
	}
}