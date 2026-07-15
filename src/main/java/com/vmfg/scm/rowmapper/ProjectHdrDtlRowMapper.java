package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.ProjectHdrDtlEntity;

public class ProjectHdrDtlRowMapper implements RowMapper<ProjectHdrDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectHdrDtlRowMapper.class);
	@Override
	public ProjectHdrDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjectHdrDtlEntity projectHdrDtlEntity=new ProjectHdrDtlEntity();
		try {
			
			if (columnExists(rs, "PROJECT_CODE")) {
				projectHdrDtlEntity.setProjectCode(rs.getString("PROJECT_CODE"));
			}

			if (columnExists(rs, "TRANSACTION_NO")) {
				projectHdrDtlEntity.setTransactionNo(rs.getString("TRANSACTION_NO"));
			}
			if (columnExists(rs, "CUSTOMER_NAME")) {
				projectHdrDtlEntity.setCustomerName(rs.getString("CUSTOMER_NAME"));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
				projectHdrDtlEntity.setProjectName(rs.getString("PROJECT_NAME"));
			}
			if (columnExists(rs, "SE_ID")) {
				projectHdrDtlEntity.setSeId(rs.getString("SE_ID"));
			}

		}catch(Exception ex) {
			logger.error("ProjectHdrDtlRowMapper  Method Exception" + ex);

		}
		return projectHdrDtlEntity;
	}



	//column checking purpose (column is there or not)
	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}
}
