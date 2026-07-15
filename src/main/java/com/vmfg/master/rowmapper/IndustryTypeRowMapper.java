package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.IndustryTypeEntity;

public class IndustryTypeRowMapper implements RowMapper<IndustryTypeEntity>{
	private static final Logger logger = LoggerFactory.getLogger(IndustryTypeRowMapper.class);

	@Override
	public IndustryTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndustryTypeEntity res = new IndustryTypeEntity();
		try {
			if (columnExists(rs, "IT_CODE")) {
				res.setItCode(rs.getString("IT_CODE"));
			}
			if (columnExists(rs, "IT_DESC")) {
				res.setItDesc(rs.getString("IT_DESC"));
			}
		}catch(Exception ex) {
			logger.error("IndustryTypeRowMapper  Method Exception" + ex);
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
