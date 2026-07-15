package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.IndentTypeMstTblEntity;

public class IndentTypeMstTblRowMapper implements RowMapper<IndentTypeMstTblEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentTypeMstTblRowMapper.class);

	@Override
	public IndentTypeMstTblEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentTypeMstTblEntity res = new IndentTypeMstTblEntity();
		try {
			res.setIndentTypeCode(rs.getString("INDENT_TYPE_CODE"));
			res.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
		} catch (Exception ex) {
			logger.error("IndentTypeMstTblRowMapper  Method Exception" + ex);
		}
		return res;
	}
}

