package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.PoStatusEntity;

public class PoStatusRowMapper implements RowMapper<PoStatusEntity> {
    @Override
    public PoStatusEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    	PoStatusEntity entity = new PoStatusEntity();
        entity.setPoSId(rs.getString("PO_S_ID"));
        entity.setPoId(rs.getString("PO_ID"));
        entity.setSeqNo(rs.getString("SEQUENCE_NO"));
        entity.setSeqStatus(rs.getString("SEQUENCE_STATUS"));
        entity.setSeqStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
        entity.setRemarks(rs.getString("REMARKS"));
        entity.setUpdatedBy(rs.getString("UPDATED_BY"));
        entity.setUpdatedOn(rs.getString("UPDATED_ON"));
        entity.setTenantId(rs.getString("TENANT_ID"));
        entity.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
        return entity;
    }
}