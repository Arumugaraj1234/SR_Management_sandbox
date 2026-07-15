package com.vmfg.inventory.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.inventory.entity.PocodeAndUnitRate;

public class PocodeAndUnitRateRowMapper  implements RowMapper<PocodeAndUnitRate> {
	private static final Logger logger = LoggerFactory.getLogger(PocodeAndUnitRateRowMapper.class);

	@Override
	public PocodeAndUnitRate mapRow(ResultSet rs, int rowNum) throws SQLException {
		PocodeAndUnitRate ldt = new PocodeAndUnitRate();
		try {
			ldt.setPoCode(rs.getString("PO_CODE"));
			ldt.setUnitRate(rs.getString("UNIT_RATE"));
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("PocodeAndUnitRateRowMapper  Method Exception" + e);
		}
		return ldt;
	}


}
