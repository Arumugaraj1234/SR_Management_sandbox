package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.FileUploadConfigEntity;

public class FileUploadConfigRowMapper implements RowMapper<FileUploadConfigEntity> {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadConfigRowMapper.class);

	@Override
	public FileUploadConfigEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		FileUploadConfigEntity res =  new FileUploadConfigEntity();
		try {
			if (columnExists(rs, "DOCUMENT_TYPE_CODE")) {
				res.setDocCode(rs.getString("DOCUMENT_TYPE_CODE"));
			}
			if (columnExists(rs, "DESCRIPTION")) {
				res.setDesc(rs.getString("DESCRIPTION"));
			}
			if (columnExists(rs, "FU_CODE")) {
				res.setFuCode(rs.getString("FU_CODE"));
			}
		}catch(Exception e) {
			logger.error("FileUploadConfigRowMapper  Method Exception" + e);
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
