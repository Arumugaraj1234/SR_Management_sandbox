package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentHdrDropDownEntity;

public class IndentHdrDropDownRowMapper implements RowMapper<IndentHdrDropDownEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectDtlsRowMapper.class);

	@Override
	public IndentHdrDropDownEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		IndentHdrDropDownEntity ph = new IndentHdrDropDownEntity();
		try {
			ph.setIndentCode(row.getString("INDENT_CODE"));
			ph.setIndentId(row.getString("INDENT_ID"));
			ph.setExpectedDeliveryDate(row.getString("EXPECTED_DELIVERY_DATE"));
		} catch (Exception e) {
			logger.error("IndentHdrDropDownRowMapper Exception--->" + e);
		}
		return ph;
	}
}