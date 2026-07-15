package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetRemarksDtlEntity;

public class GetRemarksDtlRowMapper implements RowMapper<GetRemarksDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetRemarksDtlRowMapper.class);

	@Override
	public GetRemarksDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetRemarksDtlEntity result = new GetRemarksDtlEntity();
		try {

			if (columnExists(rs, "RQ_DTL_ID")) {
				result.setRqDtlId((rs.getString("RQ_DTL_ID")));
			}
			if (columnExists(rs, "REMARKS")) {
				result.setRemarks((rs.getString("REMARKS")));
			}
			if (columnExists(rs, "RQ_ID")) {
				result.setRqId((rs.getString("RQ_ID")));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				result.setEmpName((rs.getString("EMPLOYEE_FIRSTNAME")));
			}
			if (columnExists(rs, "REQUESTED_DATETIME")) {
				result.setRequestedDateTime((rs.getString("REQUESTED_DATETIME")));
			}
			
		} catch (Exception ex) {
			logger.error("GetRemarksDtlRowMapper error " + ex);
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
