package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.response.KeyAreaIndentId;

public class KeyAreaIndentIdRowMapper implements RowMapper<KeyAreaIndentId> {
	private static final Logger logger = LoggerFactory.getLogger(KeyAreaIndentIdRowMapper.class);

	@Override
	public KeyAreaIndentId mapRow(ResultSet rs, int rowNum) throws SQLException {
		KeyAreaIndentId res = new KeyAreaIndentId();
		try {
			res.setKeyId(rs.getString("PKA_ID"));
			res.setKeyName(rs.getString("PK_DESC"));
			res.setIndentId(rs.getString("INDENT_ID"));
		} catch (Exception ex) {
			logger.error("KeyAreaIndentIdRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
