package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.response.KeySubArea;

public class DesignSubAreaRowMapper implements RowMapper<KeySubArea> {
	private static final Logger logger = LoggerFactory.getLogger(DesignSubAreaRowMapper.class);

	@Override
	public KeySubArea mapRow(ResultSet rs, int rowNum) throws SQLException {
		KeySubArea res = new KeySubArea();
		try {
			res.setKeyId(rs.getString("PKSA_ID"));
			res.setKeyName(rs.getString("PSK_DESC"));
		} catch (Exception ex) {
			logger.error("KeyArea  Method Exception" + ex);
		}
		return res;
	}

}
