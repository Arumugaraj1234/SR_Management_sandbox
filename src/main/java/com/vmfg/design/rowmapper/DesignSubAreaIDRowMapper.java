package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.response.KeyArea_ID;

public class DesignSubAreaIDRowMapper implements RowMapper<KeyArea_ID> {
	private static final Logger logger = LoggerFactory.getLogger(DesignSubAreaIDRowMapper.class);

	@Override
	public KeyArea_ID mapRow(ResultSet rs, int rowNum) throws SQLException {
		KeyArea_ID res = new KeyArea_ID();
		try {
			res.setKeyId(rs.getString("PKSE_ID"));
		} catch (Exception ex) {
			logger.error("DesignSubAreaIDRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
