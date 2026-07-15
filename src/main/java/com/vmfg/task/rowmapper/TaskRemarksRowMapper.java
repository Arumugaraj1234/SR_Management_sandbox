package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.response.GetRemarksByIdResponse;

public class TaskRemarksRowMapper implements RowMapper<GetRemarksByIdResponse> {
	private static final Logger logger = LoggerFactory.getLogger(TaskEntryDtlRowMapper.class);

	@Override
	public GetRemarksByIdResponse mapRow(ResultSet row, int rowNum) throws SQLException {
		GetRemarksByIdResponse tm = new GetRemarksByIdResponse();
		try {

			tm.setEmployeeId(row.getString("EMPLOYEE_ID"));
			tm.setRemarks(row.getString("REMARKS"));
			tm.setStatus(row.getString("STATUS"));
			tm.setTeDtlId(row.getString("TE_STATUS_DTL_ID"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTranscactionDatetime(row.getString("TRANSCACTION_DATETIME"));
			tm.setEmployeeName(row.getString("EMPLOYEE_FIRSTNAME"));
			tm.setStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));

		} catch (Exception e) {
			logger.error("TaskRemarksRowMapper Exception--->" + e);
		}
		return tm;
	}
}
