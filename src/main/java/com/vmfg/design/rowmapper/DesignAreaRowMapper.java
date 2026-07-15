package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.response.KeyArea;

public class DesignAreaRowMapper implements RowMapper<KeyArea> {
	private static final Logger logger = LoggerFactory.getLogger(DesignAreaRowMapper.class);

	@Override
	public KeyArea mapRow(ResultSet rs, int rowNum) throws SQLException {
		KeyArea res = new KeyArea();
		try {
			res.setKeyId(rs.getString("PKA_ID"));
			res.setKeyName(rs.getString("PK_DESC"));
		} catch (Exception ex) {
			logger.error("KeyArea  Method Exception" + ex);
		}
		return res;
	}

}
