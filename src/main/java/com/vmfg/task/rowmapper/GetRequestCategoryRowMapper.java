package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetRequestCategoryEntity;

public class GetRequestCategoryRowMapper implements RowMapper<GetRequestCategoryEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetRequestCategoryRowMapper.class);

	@Override
	public GetRequestCategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetRequestCategoryEntity result = new GetRequestCategoryEntity();
		try {

			if (columnExists(rs, "REQ_CAT_DESC")) {
				result.setCateDesc((rs.getString("REQ_CAT_DESC")));
			}
			if (columnExists(rs, "REQ_CATE_ID")) {
				result.setCateId((rs.getString("REQ_CATE_ID")));
			}

		} catch (Exception ex) {
			logger.error("GetRequestCategoryRowMapper error " + ex);
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
