package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.response.getelementHdrDistinctResponse;

public class ElementHdrDistinctRowMapper implements RowMapper<getelementHdrDistinctResponse> {
	private static final Logger logger = LoggerFactory.getLogger(ElementHdrDistinctRowMapper.class);

	@Override
	public getelementHdrDistinctResponse mapRow(ResultSet row, int rowNum) throws SQLException {
		getelementHdrDistinctResponse ph = new getelementHdrDistinctResponse();
		try {
			ph.setElementhdr(row.getString("ELEMENT_DESC"));
		} catch (Exception e) {
			logger.error("ElementHdrDistinctRowMapper Exception--->" + e);
		}
		return ph;
	}

}
