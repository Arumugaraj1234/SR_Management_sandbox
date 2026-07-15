package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.QIConfigHdrEntity;

public class QIConfigHdrRowMapper implements RowMapper<QIConfigHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(QualityInspectionHdrRowMapper.class);

	@Override
	public QIConfigHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		QIConfigHdrEntity qi = new QIConfigHdrEntity();
		try {
			qi.setQicHdrId(rs.getString("QIC_HDR_ID"));
	        qi.setQicName(rs.getString("QIC_NAME"));
	        qi.setQicCreatedOn(rs.getString("QIC_CREATED_ON"));
	        qi.setQicCreatedBy(rs.getString("QIC_CREATED_BY"));
	        qi.setInspectionType(rs.getString("INSPECTION_TYPE"));
	        qi.setIsActive(rs.getString("IS_ACTIVE"));
	        qi.setTenantId(rs.getString("TENANT_ID"));
	        
		} catch (Exception e) {
			logger.error("QIConfigHdrRowMapper Exception--->" + e);
		}
		return qi;
	}
}