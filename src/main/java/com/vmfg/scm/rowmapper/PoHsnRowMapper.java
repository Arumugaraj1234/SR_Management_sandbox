package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.PoHsnEntity;

public class PoHsnRowMapper implements RowMapper<PoHsnEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectDtlsRowMapper.class);

	@Override
	public PoHsnEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		PoHsnEntity ph = new PoHsnEntity();
		try {
			ph.setKey(row.getString("S_NO"));
			ph.setValue(row.getString("HSN_CODE"));
			ph.setUomCode(row.getString("UOM_CODE"));
			ph.setUomDesc(row.getString("UOM_SHORT_DESCRIPTION"));
		} catch (Exception e) {
			logger.error("ProjectDtlsRowMapper Exception--->" + e);
		}
		return ph;
	}
}
