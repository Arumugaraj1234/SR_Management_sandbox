package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.StationDropDownEntity;

public class StationDropDownRowMapper implements RowMapper<StationDropDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(StationDropDownRowMapper.class);

	@Override
	public StationDropDownEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		StationDropDownEntity st = new StationDropDownEntity();
		try {
			st.setPkaId(row.getString("PKA_ID"));
			st.setStationDesc(row.getString("PK_DESC"));
		} catch (Exception e) {
			logger.error("StationDropDownRowMapper Exception--->" + e);
		}
		return st;
	}
}
