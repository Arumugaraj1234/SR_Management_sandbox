package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.DocTypeMstEntity;

public class TypeMstRowMapper implements RowMapper<DocTypeMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TypeMstRowMapper.class);

	@Override
	public DocTypeMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DocTypeMstEntity res =  new DocTypeMstEntity();
		try {
			if (columnExists(rs, "DOCUMENT_TYPE_CODE")) {
				res.setDocCode(rs.getString("DOCUMENT_TYPE_CODE"));
			}
			if (columnExists(rs, "DOCUMENT_TYPE_DESCRIPTION")) {
				res.setDocDesc(rs.getString("DOCUMENT_TYPE_DESCRIPTION"));
			}
		}catch(Exception e) {
			logger.error("TypeMstRowMapper  Method Exception" + e);
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
