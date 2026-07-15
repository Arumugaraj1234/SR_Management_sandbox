package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.TemplateHdrNameEntity;

public class TemplateHdrNameRowMapper implements RowMapper<TemplateHdrNameEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TemplateHdrNameRowMapper.class);

	@Override
	public TemplateHdrNameEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		TemplateHdrNameEntity tm = new TemplateHdrNameEntity();
		try {
			tm.setTtHdrId(row.getString("TT_HDR_ID"));
			tm.setTTName(row.getString("TT_NAME"));
			
		} catch (Exception e) {
			logger.error("TemplateHdrNameRowMapper Exception--->" + e);
		}
		return tm;
	}

}
