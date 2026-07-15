package com.vmfg.quality.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.quality.entity.QIConfigDtlEntity;

public class QIConfigDtlRowMapper implements RowMapper<QIConfigDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(QIConfigDtlRowMapper.class);

	@Override
	public QIConfigDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		QIConfigDtlEntity qi = new QIConfigDtlEntity();
		try {
			qi.setQicDtlId(rs.getString("QIC_DTL_ID"));
	        qi.setQicHdrId(rs.getString("QIC_HDR_ID"));
	        qi.setDescription(rs.getString("DESCRIPTION"));
	        qi.setSpecification(rs.getString("SPECIFICATION"));
	        qi.setInspectionMethod(rs.getString("INSPECTION_METHOD"));
	        qi.setIsActive(rs.getString("IS_ACTIVE"));
	        qi.setTenantId(rs.getString("TENANT_ID"));
	        
		} catch (Exception e) {
			logger.error("QIConfigDtlRowMapper Exception--->" + e);
		}
		return qi;
	}
	
}