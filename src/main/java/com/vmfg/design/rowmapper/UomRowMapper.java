package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.UomEntity;

public class UomRowMapper  implements RowMapper<UomEntity> {
	private static final Logger logger = LoggerFactory.getLogger(UomRowMapper.class);

	@Override
	public UomEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		UomEntity res = new UomEntity();
		try {
			res.setUomCode(rs.getString("UOM_CODE"));
			res.setUomDesc(rs.getString("UOM_SHORT_DESCRIPTION"));
		} catch (Exception ex) {
			logger.error("UomRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
