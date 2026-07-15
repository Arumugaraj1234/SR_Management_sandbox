package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.DocumentAppStatusDtlEntity;

public class DocumentAppStatusDtlRowMapper implements RowMapper<DocumentAppStatusDtlEntity> {
		private static final Logger logger = LoggerFactory.getLogger(DocumentAppStatusDtlRowMapper.class);

		@Override
		public DocumentAppStatusDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
			DocumentAppStatusDtlEntity tm = new DocumentAppStatusDtlEntity();
			try {
				tm.setDasId(row.getString("DAS_ID"));
				tm.setDmId(row.getString("DM_ID"));
				tm.setSequenceNo(row.getString("SEQUENCE_NO"));
				tm.setSequenceStatus(row.getString("SEQUENCE_STATUS"));
				tm.setTenantId(row.getString("TENANT_ID"));
				tm.setUpdatedOn(row.getString("UPDATED_ON"));
				tm.setUpdatedby(row.getString("UPDATED_BY"));
				tm.setSeqStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
				tm.setVersion(row.getString("VERSION"));
			} catch (Exception e) {
				logger.error("DocumentAppStatusDtlRowMapper Exception--->" + e);
			}
			return tm;
		}

	}



