package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.DocumentManagementAccessEntity;

public class DocumentManagementAccessRowMapper implements RowMapper<DocumentManagementAccessEntity>{
	private static final Logger logger = LoggerFactory.getLogger(DocumentManagementAccessRowMapper.class);
	@Override
	public DocumentManagementAccessEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DocumentManagementAccessEntity documentManagementEntity=new DocumentManagementAccessEntity();
		try {
			if (columnExists(rs, "DEPARTMENT_CODE")) {
				documentManagementEntity.setDeptCode(rs.getString("DEPARTMENT_CODE"));
			}
			if (columnExists(rs, "DEPARTMENT_NAME")) {
				documentManagementEntity.setDeptName(rs.getString("DEPARTMENT_NAME"));
			}
			if (columnExists(rs, "ENABLED_DATETIME")) {
				documentManagementEntity.setEnabledDateTime(rs.getString("ENABLED_DATETIME"));
			}
			if (columnExists(rs, "DMA_ID")) {
				documentManagementEntity.setDmaId(rs.getString("DMA_ID"));
			}

			
		}catch(Exception ex) {
			logger.error("DocumentManagementAccessRowMapper  Method Exception" + ex);

		}
		return documentManagementEntity;
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
