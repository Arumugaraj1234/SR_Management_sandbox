package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.TemplateTypeMstEntity;

public class TemplateTypeRowMapper implements RowMapper<TemplateTypeMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TemplateTypeRowMapper.class);

	@Override
	public TemplateTypeMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		TemplateTypeMstEntity res =  new TemplateTypeMstEntity();
		try {
			if (columnExists(rs, "TT_HDR_ID")) {
				res.setTtHdrId(rs.getString("TT_HDR_ID"));
			}
			if (columnExists(rs, "TT_NAME")) {
				res.setTempName(rs.getString("TT_NAME"));
			}
			if (columnExists(rs, "IS_ACTIVE")) {
				res.setIsActive(rs.getString("IS_ACTIVE"));
			}
		}catch(Exception e) {
			logger.error("TemplateTypeRowMapper  Method Exception" + e);
		}
		return res;
	}

	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

	for (int i = 1; i <= columns; i++) {
		if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
			return true;
		}
	 }
	return false;
	}

}
