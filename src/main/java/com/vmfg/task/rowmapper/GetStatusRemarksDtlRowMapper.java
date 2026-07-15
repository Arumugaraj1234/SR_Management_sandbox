package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetStatusRemarksDtlEntity;

public class GetStatusRemarksDtlRowMapper implements RowMapper<GetStatusRemarksDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetStatusRemarksDtlRowMapper.class);

	@Override
	public GetStatusRemarksDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetStatusRemarksDtlEntity result = new GetStatusRemarksDtlEntity();
		try {

			if (columnExists(rs, "REMARKS")) {
				result.setRemarks((rs.getString("REMARKS")));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				result.setEmployeeName((rs.getString("EMPLOYEE_FIRSTNAME")));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				result.setStatusDesc((rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION")));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_CODE")) {
				result.setStatusTypeCode((rs.getString("DOCUMENT_STATUS_TYPE_CODE")));
			}

		} catch (Exception ex) {
			logger.error("GetStatusRemarksDtlRowMapper error " + ex);
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
