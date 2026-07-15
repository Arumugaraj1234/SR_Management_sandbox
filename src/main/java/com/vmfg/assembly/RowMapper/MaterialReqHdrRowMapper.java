package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.MaterialReqHdrEntity;

public class MaterialReqHdrRowMapper implements RowMapper<MaterialReqHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(MaterialReqHdrRowMapper.class);

	@Override
	public MaterialReqHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MaterialReqHdrEntity result = new MaterialReqHdrEntity();
		try {

			if (columnExists(rs, "MR_CODE")) {
				result.setMrCode(rs.getString("MR_CODE"));
			}
			if (columnExists(rs, "REQUESTED_ON")) {
				result.setRequestedOn(rs.getString("REQUESTED_ON"));
			}

			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				result.setEmployeeName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}

			if (columnExists(rs, "REQUESTED_BY")) {
				result.setEmpId(rs.getString("REQUESTED_BY"));
			}

			if (columnExists(rs, "IS_COMPLETED")) {
				result.setCompleted(rs.getString("IS_COMPLETED"));
			}

			if (columnExists(rs, "IS_CANCELLED")) {
				result.setCancelled(rs.getString("IS_CANCELLED"));
			}

			if (columnExists(rs, "REASON")) {
				result.setReason(rs.getString("REASON"));
			}
			if (columnExists(rs, "MR_HDR_ID")) {
				result.setMrHdrId(rs.getString("MR_HDR_ID"));
			}
			if (columnExists(rs, "REQUEST_TYPE")) {
				result.setRequestType(rs.getString("REQUEST_TYPE"));
			}
			
		} catch (Exception ex) {
			logger.error("MaterialReqHdrRowMapper error " + ex);
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
