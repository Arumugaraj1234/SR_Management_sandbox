package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.PoDescMstEntity;

public class PoDescMstRowMapper implements RowMapper<PoDescMstEntity> {
	@Override
	public PoDescMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		PoDescMstEntity entity = new PoDescMstEntity();
		entity.setDesc(rs.getString("TYPE_DESC"));
		entity.setId(rs.getString("TYPE_ID"));
		entity.setIsActive(rs.getString("IS_ACTIVE"));
		entity.setTenantId(rs.getString("TENANT_ID"));
		return entity;
	}

}
