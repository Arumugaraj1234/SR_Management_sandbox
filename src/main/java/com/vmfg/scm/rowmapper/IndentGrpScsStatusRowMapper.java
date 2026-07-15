package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGrpScsStatusEntity;

public class IndentGrpScsStatusRowMapper implements RowMapper<IndentGrpScsStatusEntity> {
    @Override
    public IndentGrpScsStatusEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        IndentGrpScsStatusEntity entity = new IndentGrpScsStatusEntity();
        entity.setIgScsSId(rs.getString("IG_SCSS_ID"));
        entity.setIgScsId(rs.getString("IG_SCS_ID"));
        entity.setSequenceNo(rs.getString("SEQUENCE_NO"));
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