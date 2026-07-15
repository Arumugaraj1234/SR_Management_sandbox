package com.vmfg.design.rowmapper;

import com.vmfg.design.entity.IndentPartDetailsEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IndentPartDetailsRowMapper implements RowMapper<IndentPartDetailsEntity> {
    @Override
    public IndentPartDetailsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        IndentPartDetailsEntity entity = new IndentPartDetailsEntity();
        entity.setIndentId(rs.getInt("INDENT_ID"));
        entity.setIndentCode(rs.getString("INDENT_CODE"));
        entity.setStationId(rs.getInt("stationId"));
        entity.setStationNo(rs.getString("stationNo"));
        entity.setSubAssyId(rs.getInt("subAssyId"));
        entity.setSubAssyDesc(rs.getString("subAssyDesc"));
        entity.setPartNo(rs.getString("partNo"));
        entity.setPartDesc(rs.getString("partDesc"));
        return entity;
    }
}

