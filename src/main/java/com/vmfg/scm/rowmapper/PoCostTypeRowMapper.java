package com.vmfg.scm.rowmapper;

import com.vmfg.scm.entity.PoCostTypeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PoCostTypeRowMapper implements RowMapper<PoCostTypeEntity> {
    private static final Logger logger = LoggerFactory.getLogger(PoCostTypeRowMapper.class);

    @Override
    public PoCostTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        PoCostTypeEntity poCostType = new PoCostTypeEntity();
        try{
            poCostType.setPctId(rs.getString("PCT_ID"));
            poCostType.setPctDesc(rs.getString("PCT_DESC"));
            poCostType.setTenantId(rs.getString("TENANT_ID"));
            poCostType.setIsActive(rs.getString("IS_ACTIVE"));
        } catch (Exception e) {
            logger.error("PoCostTypeRowMapper Exception ----->" + e);
        }
        return poCostType;
    }
}
