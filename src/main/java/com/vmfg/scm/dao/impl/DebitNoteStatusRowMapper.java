package com.vmfg.scm.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.DebitNoteStatusEntity;

public class DebitNoteStatusRowMapper implements RowMapper<DebitNoteStatusEntity>{

	public DebitNoteStatusEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DebitNoteStatusEntity dnStatus = new DebitNoteStatusEntity();
		dnStatus.setDnId(rs.getString("DN_ID"));
		dnStatus.setRemarks(rs.getString("REMARKS"));
		dnStatus.setSeqno(rs.getString("SEQUENCE_NO"));
		dnStatus.setSeqDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
		dnStatus.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
		dnStatus.setSeqStatus(rs.getString("SEQUENCE_STATUS"));
		dnStatus.setUpdatedBy(rs.getString("UPDATED_BY"));
		dnStatus.setUpdatedOn(rs.getString("UPDATED_ON"));
		dnStatus.setTenantId(rs.getString("TENANT_ID"));
        return dnStatus;

	}

}
