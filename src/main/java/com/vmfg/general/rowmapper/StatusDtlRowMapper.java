package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.StatusDtlEntity;

public class StatusDtlRowMapper  implements RowMapper<StatusDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProcessConfigRowMapper.class);

	@Override
	public StatusDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		StatusDtlEntity tm = new StatusDtlEntity();
		try {
			tm.setEmpDesc(row.getString("EMPLOYEE_FIRSTNAME"));
			tm.setEmpId(row.getString("UPDATED_BY"));
			tm.setReferenceDoc(row.getString("REFERENCE_DOC"));
			tm.setReferenceDocDesc(row.getString("DOCUMENT_TYPE_DESCRIPTION"));
			tm.setSeqNo(row.getString("SEQUENCE_NO"));
			tm.setSeqStatus(row.getString("SEQUENCE_STATUS"));
			tm.setSeqStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			tm.setUpdatedOn(row.getString("UPDATED_ON"));
			tm.setReferenceId(row.getString("REFERENCE_ID"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setRemarks(row.getString("REMARKS"));
			} catch (Exception e) {
			logger.error("StatusDtlRowMapper Exception--->" + e);
		}
		return tm;
	}

}
