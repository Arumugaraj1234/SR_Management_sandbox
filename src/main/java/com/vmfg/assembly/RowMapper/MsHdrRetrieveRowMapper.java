package com.vmfg.assembly.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.assembly.entity.MsHdrRetrieveEntity;

public class MsHdrRetrieveRowMapper implements RowMapper<MsHdrRetrieveEntity> {
	private static final Logger logger = LoggerFactory.getLogger(MsHdrRetrieveRowMapper.class);

	@Override
	public MsHdrRetrieveEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		MsHdrRetrieveEntity result = new MsHdrRetrieveEntity();
		try {

			if (columnExists(rs, "MS_NAME")) {
				result.setMsName(rs.getString("MS_NAME"));
			}

			if (columnExists(rs, "STATUS")) {
				result.setStatus(rs.getString("STATUS"));
			}

			if (columnExists(rs, "QTY")) {
				result.setStageQty(rs.getString("QTY"));
			}

			if (columnExists(rs, "CREATED_ON")) {
				result.setCreatedOn(rs.getString("CREATED_ON"));
			}

			if (columnExists(rs, "CREATED_BY")) {
				result.setCreatedBy(rs.getString("CREATED_BY"));
			}
			if (columnExists(rs, "MS_HDR_ID")) {
				result.setMsHdrId(rs.getString("MS_HDR_ID"));
			}
			result.setPmHdrId(rs.getString("PM_HDR_ID"));

		} catch (Exception ex) {
			logger.error("MsHdrRetrieveRowMapper error " + ex);
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
