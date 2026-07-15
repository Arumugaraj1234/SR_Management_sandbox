package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.MrHdrRetrieveEntity;

public class MrHdrRetrieveRowMapper implements RowMapper<MrHdrRetrieveEntity> {
	private static final Logger logger = LoggerFactory.getLogger(MrHdrRetrieveRowMapper.class);

	@Override
	public MrHdrRetrieveEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MrHdrRetrieveEntity result = new MrHdrRetrieveEntity();
		try {

			if (columnExists(rs, "REMARKS")) {
				result.setRemarks(rs.getString("REMARKS"));
			}


			if (columnExists(rs, "CREATED_ON")) {
				result.setCreatedOn(rs.getString("CREATED_ON"));
			}

			if (columnExists(rs, "EMPLOYEE_NAME")) {
				result.setEmployeeName(rs.getString("EMPLOYEE_NAME"));
			}
			if (columnExists(rs, "EMPLOYEE_ID")) {
				result.setEmployeeId(rs.getString("EMPLOYEE_ID"));
			}
			if (columnExists(rs, "MRH_ID")) {
				result.setMrhId(rs.getString("MRH_ID"));
			}
			if (columnExists(rs, "IS_COMPLETED")) {
				result.setIsCompleted(rs.getString("IS_COMPLETED"));
			}
			if (columnExists(rs, "IS_CANCELED")) {
				result.setIsCanceled(rs.getString("IS_CANCELED"));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				result.setStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_CODE")) {
				result.setStatusCode(rs.getString("DOCUMENT_STATUS_TYPE_CODE"));
			}
			if (columnExists(rs, "SEQUENCE_NO")) {
				result.setSeqNo(rs.getString("SEQUENCE_NO"));
			}
			if (columnExists(rs, "LAST_UPDATED_DATETIME")) {
				result.setLastUpdatedBy(rs.getString("LAST_UPDATED_DATETIME"));
			}

		} catch (Exception ex) {
			logger.error("MrHdrRetrieveRowMapper error " + ex);
		}

		return result;
	}

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
