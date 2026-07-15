package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.DocumentTypeMstEntity;

public class DocTypeMstRowMapper implements RowMapper<DocumentTypeMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocTypeMstRowMapper.class);

	@Override
	public DocumentTypeMstEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		DocumentTypeMstEntity tm = new DocumentTypeMstEntity();
		try { 
		
			tm.setDocTypeCode(row.getString("DOCUMENT_TYPE_CODE"));
			tm.setDocTypeDesc(row.getString("DOCUMENT_TYPE_DESCRIPTION"));
			tm.setIsActive(row.getString("IS_ACTIVE"));
			tm.setMstTableName(row.getString("MASTER_TABLE_NAME"));
			tm.setPmDesc(row.getString("PM_DESC"));
			tm.setPmId(row.getString("PM_ID"));
			tm.setRefSlaveId(row.getString("REFERENCE_SLAVE_ID"));
			tm.setRefTableName(row.getString("REFERENCE_TABLE_NAME"));
			tm.setStatusTableName(row.getString("STATUS_TABLE_NAME"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setStgCode(row.getString("STG_CODE"));
			tm.setMstColumnName(row.getString("MASTER_TABLE_ID"));
		} catch (Exception e) {
			logger.error("DocumentTypeMstRowMapper Exception--->" + e);
		}
		return tm;
	}

}
