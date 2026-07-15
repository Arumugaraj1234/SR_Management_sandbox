package com.vmfg.timesheet.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.timesheet.response.GetTimeDtlByHdrEntity;

public class GetTimeDtlByHdrRowMapper implements RowMapper<GetTimeDtlByHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetTimeDtlByHdrRowMapper.class);

	@Override
	public GetTimeDtlByHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetTimeDtlByHdrEntity result = new GetTimeDtlByHdrEntity();
		try {
			if (columnExists(rs, "T_DTL_ID")) {
				result.setTDtlId((rs.getString("T_DTL_ID")));
			}
			if (columnExists(rs, "T_HDR_ID")) {
				result.setTHdrId((rs.getString("T_HDR_ID")));
			}
			if (columnExists(rs, "TE_DTL_ID")) {
				result.setTeDtlId((rs.getString("TE_DTL_ID")));
			}
			if (columnExists(rs, "TD_DTL_ID")) {
				result.setTdDtlId((rs.getString("TD_DTL_ID")));
			}
			if (columnExists(rs, "TIMESHEET_DTL")) {
				result.setTimeSheetDtl((rs.getString("TIMESHEET_DTL")));
			}
			if (columnExists(rs, "TIMESHEET_HRS")) {
				result.setTimeSheetHrs((rs.getString("TIMESHEET_HRS")));
			}
			if (columnExists(rs, "TENANT_ID")) {
				result.setTenantId((rs.getString("TENANT_ID")));
			}

		} catch (Exception ex) {
			logger.error("GetTimeDtlByHdrRowMapper error " + ex);
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