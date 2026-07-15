package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class EPMDurationRowMapper implements RowMapper<EPMDurationEntity>{
	private static final Logger logger = LoggerFactory.getLogger(EPMDurationRowMapper.class);
	@Override
	public EPMDurationEntity mapRow(ResultSet row, int rowNum) throws SQLException {		
		EPMDurationEntity epm = new EPMDurationEntity();
		try {
			epm.setEquipId(row.getString(""));
			epm.setEquipDes(row.getString(""));
			epm.setOprId(row.getString(""));
			epm.setOprDesc(row.getString(""));
			epm.setProdCode(row.getString(""));
			epm.setProdDesc(row.getString(""));
			epm.setRouteId(row.getString(""));
			epm.setSequence(row.getString(""));
			epm.setDuration(row.getString(""));
		}catch(Exception ex) {
			logger.error("EPMDurationRowMapper method Exception"+ex);
		}
		return null;
	}

}
