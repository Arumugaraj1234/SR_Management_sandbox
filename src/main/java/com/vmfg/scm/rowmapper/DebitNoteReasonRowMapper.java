package com.vmfg.scm.rowmapper;

import com.vmfg.scm.entity.DebitNoteEntity;
import com.vmfg.scm.entity.PoCostTypeEntity;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DebitNoteReasonRowMapper implements RowMapper<DebitNoteEntity> {

    private static final Logger logger = LoggerFactory.getLogger(DebitNoteEntity.class);

    @Override
    public DebitNoteEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        DebitNoteEntity debitNote = new DebitNoteEntity();
        try{
            debitNote.setDnrId(rs.getString("DNR_ID"));
            debitNote.setDnrDesc(rs.getString("DNR_REASON"));
            debitNote.setTenantId(rs.getString("TENANT_ID"));
            debitNote.setIsActive(rs.getString("IS_ACTIVE"));
        } catch (Exception e) {
            logger.error("DebitNoteReasonRowMapper Exception ----->" + e);
        }
        return debitNote;
    }
}
