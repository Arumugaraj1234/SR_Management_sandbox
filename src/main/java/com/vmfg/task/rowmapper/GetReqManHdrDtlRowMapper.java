package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetReqManHdrDtlEntity;

public class GetReqManHdrDtlRowMapper implements RowMapper<GetReqManHdrDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetReqManHdrDtlRowMapper.class);

	@Override
	public GetReqManHdrDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetReqManHdrDtlEntity result = new GetReqManHdrDtlEntity();
		try {
			
			if (columnExists(rs, "REQ_CAT_DESC")) {
				result.setReqCategoryDesc((rs.getString("REQ_CAT_DESC")));
			}
			if (columnExists(rs, "DUE_DATE")) {
				result.setDueDate((rs.getString("DUE_DATE")));
			}
			if (columnExists(rs, "REQUESTED_BY_ID")) {
				result.setRequestedById((rs.getString("REQUESTED_BY_ID")));
			}
			if (columnExists(rs, "REQUESTED_TO_ID")) {
				result.setRequestedToId((rs.getString("REQUESTED_TO_ID")));
			}
			if (columnExists(rs, "TICKET_REPORTER_ID")) {
				result.setTicketReporterId((rs.getString("TICKET_REPORTER_ID")));
			}
			if (columnExists(rs, "REQUESTED_BY_DEPT_NAME")) {
				result.setRequestedByDeptName((rs.getString("REQUESTED_BY_DEPT_NAME")));
			}
			if (columnExists(rs, "REQUESTED_TO_DEPT_NAME")) {
				result.setRequestedToDeptName((rs.getString("REQUESTED_TO_DEPT_NAME")));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				result.setSeqStatusDesc((rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION")));
			}
			if (columnExists(rs, "PROJECT_DESCRIPTION")) {
				result.setProjectDesc((rs.getString("PROJECT_DESCRIPTION")));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
				result.setProjectName((rs.getString("PROJECT_NAME")));
			}
			if (columnExists(rs, "REQUESTED_BY_NAME")) {
				result.setRequestedByName((rs.getString("REQUESTED_BY_NAME")));
			}
			if (columnExists(rs, "REQUESTED_TO_NAME")) {
				result.setRequestedToName((rs.getString("REQUESTED_TO_NAME")));
			}
			if (columnExists(rs, "TICKET_REPORTER_NAME")) {
				result.setTicketReporterName((rs.getString("TICKET_REPORTER_NAME")));
			}

			if (columnExists(rs, "PM_HDR_ID")) {
				result.setPmHdrId((rs.getString("PM_HDR_ID")));
			}
			if (columnExists(rs, "RQ_ID")) {
				result.setRqId((rs.getString("RQ_ID")));
			}
			if (columnExists(rs, "REQUEST_CATEGORY")) {
				result.setReqCategory((rs.getString("REQUEST_CATEGORY")));
			}

			if (columnExists(rs, "REQUEST_NAME")) {
				result.setReqName((rs.getString("REQUEST_NAME")));
			}
			if (columnExists(rs, "REQUEST_DESCRIPTION")) {
				result.setReqDesc((rs.getString("REQUEST_DESCRIPTION")));
			}
			if (columnExists(rs, "REQUESTED_TO_DEPT")) {
				result.setRequestedToDept((rs.getString("REQUESTED_TO_DEPT")));
			}
			if (columnExists(rs, "REQUESTED_BY_DEPT")) {
				result.setRequestedByDept((rs.getString("REQUESTED_BY_DEPT")));
			}
			if (columnExists(rs, "SEQUENCE_NO")) {
				result.setSeqNo((rs.getString("SEQUENCE_NO")));
			}
			if (columnExists(rs, "SEQUENCE_STATUS")) {
				result.setSeqStatusCode((rs.getString("SEQUENCE_STATUS")));
			}
			if (columnExists(rs, "IS_COMPLETED")) {
				result.setIsCompleted((rs.getString("IS_COMPLETED")));
			}
			if (columnExists(rs, "TENANT_ID")) {
				result.setTenantId((rs.getString("TENANT_ID")));
			}
			if (columnExists(rs, "REQUESTED_DATE")) {
				result.setRequestedDate((rs.getString("REQUESTED_DATE")));
			}
			if (columnExists(rs, "REQUESTED_DATETIME")) {
				result.setReqRemarksDateTime((rs.getString("REQUESTED_DATETIME")));
			}
			if (columnExists(rs, "CLOSED_DATE")) {
				result.setClosedDate((rs.getString("CLOSED_DATE")));
			}
			if (columnExists(rs, "PROJECT_CODE")) {
				result.setProjectCode((rs.getString("PROJECT_CODE")));
			}
			
		} catch (Exception ex) {
			logger.error("GetReqManHdrDtlRowMapper error " + ex);
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
