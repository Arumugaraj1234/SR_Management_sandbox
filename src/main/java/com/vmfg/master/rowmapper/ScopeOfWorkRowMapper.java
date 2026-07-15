package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.ScopOfWorkEntity;

public class ScopeOfWorkRowMapper implements RowMapper<ScopOfWorkEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ScopeOfWorkRowMapper.class);

	@Override
	public ScopOfWorkEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ScopOfWorkEntity departmentInfoEntity = new ScopOfWorkEntity();
		try {
			if (columnExists(rs, "SOW_CODE")) {
				departmentInfoEntity.setSowCode(rs.getString("SOW_CODE"));
			}
			if (columnExists(rs, "SOW_DESC")) {
				departmentInfoEntity.setSowDesc(rs.getString("SOW_DESC"));
			}

		} catch (Exception ex) {
			logger.error("ScopeOfWorkRowMapper  Method Exception" + ex);

		}
		return departmentInfoEntity;
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
