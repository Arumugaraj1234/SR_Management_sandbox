package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetTaskHdrAndDtlIdEntity;

public class GetTaskHdrAndDtlIdRowMapper implements RowMapper<GetTaskHdrAndDtlIdEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetTaskHdrAndDtlIdRowMapper.class);

	@Override
	public GetTaskHdrAndDtlIdEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetTaskHdrAndDtlIdEntity result = new GetTaskHdrAndDtlIdEntity();
		try {

			if (columnExists(rs, "TE_HDR_ID")) {
				result.setTeHdrId((rs.getString("TE_HDR_ID")));
			}
			if (columnExists(rs, "TE_DTl_ID")) {
				result.setTeDtlId((rs.getString("TE_DTl_ID")));
			}

		} catch (Exception ex) {
			logger.error("GetTaskHdrAndDtlIdRowMapper error " + ex);
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
