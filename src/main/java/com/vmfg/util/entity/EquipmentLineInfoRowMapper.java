package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class EquipmentLineInfoRowMapper implements RowMapper<EquipmentLineInfo> {
	private static final Logger logger = LoggerFactory.getLogger(EquipmentLineInfoRowMapper.class);

	@Override
	public EquipmentLineInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		EquipmentLineInfo tm = new EquipmentLineInfo();
		try {
			tm.setLineDesc(row.getString("LINE_MST_DESCRIPTION"));
			tm.setEquipmentDesc(row.getString("EQUIPMENT_DESCRIPTION"));

		} catch (Exception e) {
			logger.error("EquipmentLineInfoRowMapper Exception--->" + e);
		}
		return tm;
	}
}
