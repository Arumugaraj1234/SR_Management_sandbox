package com.vmfg.finance.rowmapper;

import com.vmfg.finance.entity.PraStatusEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PraStatusRowMapper implements RowMapper<PraStatusEntity> {

    @Override
    public PraStatusEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        PraStatusEntity praStatus = new PraStatusEntity();
        praStatus.setPraSID(rs.getString("PRA_S_ID"));
        praStatus.setPraID(rs.getString("PRA_ID"));
        praStatus.setSequenceNo(rs.getString("SEQUENCE_NO"));
        praStatus.setSeqStatus(rs.getString("SEQUENCE_STATUS"));
        praStatus.setSeqStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
        praStatus.setRemarks(rs.getString("REMARKS"));
        praStatus.setUpdatedBy(rs.getString("UPDATED_BY"));
        praStatus.setUpdatedOn(rs.getString("UPDATED_ON"));
        praStatus.setTenantId(rs.getString("TENANT_ID"));
        praStatus.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
        return praStatus;
    }
}
