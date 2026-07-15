package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.TaskEntryDtlEntity;

public class TaskEntryDtlRowMapper implements RowMapper<TaskEntryDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskEntryDtlRowMapper.class);

	@Override
	public TaskEntryDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		TaskEntryDtlEntity tm = new TaskEntryDtlEntity();
		try {
			tm.setTeHdrId(row.getString("TE_HDR_ID"));
			tm.setActivityName(row.getString("ACTIVITY_NAME"));
			tm.setApprovalSeq(row.getString("APPROVAL_SEQ"));
			tm.setApprovalStatus(row.getString("APPROVAL_STATUS"));
			tm.setCompletedDate(row.getString("COMPLETED_DATE"));
			tm.setDueDate(row.getString("DUE_DATE"));
			tm.setIsCompleted(row.getString("IS_COMPLETED"));
			tm.setPlannedCompletedDate(row.getString("PLANNED_COMPLETED_DATE"));
			tm.setPlannedStartDate(row.getString("PLANNED_START_DATE"));
			tm.setTeDtlId(row.getString("TE_DTl_ID"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTtDtlId(row.getString("TT_DTL_ID"));
			tm.setApprovalStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			tm.setAssignTo(row.getString("ASSIGNED_TO"));
			tm.setAssignToDesc(row.getString("EMPLOYEE_FIRSTNAME"));
			tm.setActualStartDate(row.getString("ASSIGNED_TO"));
			tm.setCompletePtg(row.getString("COMPLETED_PTG"));
		} catch (Exception e) {
			logger.error("TaskEntryDtlRowMapper Exception--->" + e);
		}
		return tm;
	}

}
