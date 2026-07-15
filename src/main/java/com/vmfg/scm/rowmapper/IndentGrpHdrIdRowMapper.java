package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGrpHdrIdEntity;

public class IndentGrpHdrIdRowMapper implements RowMapper<IndentGrpHdrIdEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGrpHdrIdRowMapper.class);

	@Override
	public IndentGrpHdrIdEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentGrpHdrIdEntity result = new IndentGrpHdrIdEntity();
		try {
			result.setIgHdrId(rs.getString("IG_HDR_ID"));
		} catch (Exception ex) {
			logger.error("IndentGrpHdrIdRowMapper error " + ex);
		}
		return result;
	}

}
