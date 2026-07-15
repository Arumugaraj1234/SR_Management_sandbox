package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.ProjectDtlsEntity;

public class ProjectDtlsRowMapper implements RowMapper<ProjectDtlsEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectDtlsRowMapper.class);

	@Override
	public ProjectDtlsEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		ProjectDtlsEntity ph = new ProjectDtlsEntity();
		try {
			ph.setProjectCode(row.getString("PROJECT_CODE"));
			ph.setProjectId(row.getString("PROJECT_ID"));
			ph.setProjectName(row.getString("PROJECT_NAME"));
			ph.setCustomerName(row.getString("CUSTOMER_NAME"));
			
			if (columnExists(row, "DE_HDR_ID")) {
				ph.setMasterId(row.getString("DE_HDR_ID"));
			}
		} catch (Exception e) {
			logger.error("ProjectDtlsRowMapper Exception--->" + e);
		}
		return ph;
	}
	
	//column checking purpose (column is there or not)
		private boolean columnExists(ResultSet row, String columnName) throws SQLException {
			ResultSetMetaData metaData = row.getMetaData();
			int columns = metaData.getColumnCount();

			for (int i = 1; i <= columns; i++) {
				if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
					return true;
				}
			}

			return false;
		}
}
