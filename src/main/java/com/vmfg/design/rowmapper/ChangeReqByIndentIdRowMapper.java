package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ChangeRequestIndentEntity;

public class ChangeReqByIndentIdRowMapper implements RowMapper<ChangeRequestIndentEntity> {
	
	private static final Logger logger = LoggerFactory.getLogger(ChangeRequestIndentDtlRowMapper.class);

	@Override
	public ChangeRequestIndentEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ChangeRequestIndentEntity res = new ChangeRequestIndentEntity();
		try {
			res.setIndentId(rs.getString("INDENT_ID"));
			res.setIndentCode(rs.getString("INDENT_CODE"));
			res.setTenantId(rs.getString("TENANT_ID"));
		} catch (Exception ex) {
			logger.error("ChangeRequestIndentDtlRowMapper  Method Exception" + ex);
		}
		return res;
	}
}
