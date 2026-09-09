package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.PjsIndentBreakdownEntity;

public class PjsIndentBreakdownRowMapper implements RowMapper<PjsIndentBreakdownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(PjsIndentBreakdownRowMapper.class);

	@Override
	public PjsIndentBreakdownEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		PjsIndentBreakdownEntity e = new PjsIndentBreakdownEntity();
		try {
			e.setIndentId(row.getString("INDENT_ID"));
			e.setIndentCode(row.getString("INDENT_CODE"));
			e.setIndentType(row.getString("INDENT_TYPE"));
			e.setSubAssembly(row.getString("SUB_ASSEMBLY"));
			e.setPartCount(row.getString("PART_COUNT"));
		} catch (Exception ex) {
			logger.error("PjsIndentBreakdownRowMapper Exception--->" + ex);
		}
		return e;
	}
}
