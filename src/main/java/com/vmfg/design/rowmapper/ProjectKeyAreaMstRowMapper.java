package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.ProjectKeyAreaMstEntity;

public class ProjectKeyAreaMstRowMapper implements RowMapper<ProjectKeyAreaMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectKeyAreaMstRowMapper.class);

	@Override
	public ProjectKeyAreaMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjectKeyAreaMstEntity res = new ProjectKeyAreaMstEntity();
		try {
			if(columnExists(rs, "PK_ID")) {
				res.setPkId(rs.getString("PK_ID"));
				}
			if(columnExists(rs, "PSK_ID")) {
				res.setKeyId(rs.getString("PSK_ID"));
				}
			if(columnExists(rs, "PK_DESC")) {
				res.setKeyName(rs.getString("PK_DESC"));
				}
			if(columnExists(rs, "PSK_DESC")) {
				res.setKeyName(rs.getString("PSK_DESC"));
				}
			if(columnExists(rs, "CODE")) {
				res.setCode(rs.getString("CODE"));
				}
			
			
			
			if(columnExists(rs, "PKSA_ID")) {
			res.setPkaId(rs.getString("PKSA_ID"));
			}
			if(columnExists(rs, "PKA_ID")) {
			res.setPkaId(rs.getString("PKA_ID"));
			}
		} catch (Exception ex) {
			logger.error("ProjectKeyAreaMstRowMapper  Method Exception" + ex);
		}
		return res;
	}

	//column checking purpose (column is there or not)
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
