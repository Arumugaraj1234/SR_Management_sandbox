package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetTaskEntryDtlEntity;

public class GetTaskEntryDtlRowMapper implements RowMapper<GetTaskEntryDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskEntryDtlRowMapper.class);

	@Override
	public GetTaskEntryDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetTaskEntryDtlEntity tm = new GetTaskEntryDtlEntity();
		try {
			  if (columnExists(row, "TE_HDR_ID")) {
		            tm.setTeHdrId(row.getString("TE_HDR_ID"));
		        }
		        if (columnExists(row, "ACTIVITY_NAME")) {
		            tm.setActivityName(row.getString("ACTIVITY_NAME"));
		        }
		        if (columnExists(row, "APPROVAL_SEQ")) {
		            tm.setApprovalSeq(row.getString("APPROVAL_SEQ"));
		        }
		        if (columnExists(row, "APPROVAL_STATUS")) {
		            tm.setApprovalStatus(row.getString("APPROVAL_STATUS"));
		        }
		        if (columnExists(row, "COMPLETED_DATE")) {
		            tm.setCompletedDate(row.getString("COMPLETED_DATE"));
		        }
		        if (columnExists(row, "DUE_DATE")) {
		            tm.setDueDate(row.getString("DUE_DATE"));
		        }
		        if (columnExists(row, "IS_COMPLETED")) {
		            tm.setIsCompleted(row.getString("IS_COMPLETED"));
		        }
		        if (columnExists(row, "PLANNED_COMPLETED_DATE")) {
		            tm.setPlannedCompletedDate(row.getString("PLANNED_COMPLETED_DATE"));
		        }
		        if (columnExists(row, "PLANNED_START_DATE")) {
		            tm.setPlannedStartDate(row.getString("PLANNED_START_DATE"));
		        }
		        if (columnExists(row, "TE_DTL_ID")) {
		            tm.setTeDtlId(row.getString("TE_DTL_ID"));
		        }
		        if (columnExists(row, "TENANT_ID")) {
		            tm.setTenantId(row.getString("TENANT_ID"));
		        }
		        if (columnExists(row, "TT_DTL_ID")) {
		            tm.setTtDtlId(row.getString("TT_DTL_ID"));
		        }
		        if (columnExists(row, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
		            tm.setApprovalStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
		        }
		        if (columnExists(row, "ASSIGNED_TO")) {
		            tm.setAssignTo(row.getString("ASSIGNED_TO"));
		        }
		        if (columnExists(row, "EMPLOYEE_FIRSTNAME")) {
		            tm.setAssignToDesc(row.getString("EMPLOYEE_FIRSTNAME"));
		        }
		        if (columnExists(row, "ACTUAL_START_DATE")) {
		            tm.setActualStartDate(row.getString("ACTUAL_START_DATE"));
		        }
		        if (columnExists(row, "COMPLETED_PTG")) {
		            tm.setCompletePtg(row.getString("COMPLETED_PTG"));
		        }
		        if (columnExists(row, "TASK_TYPE_CODE")) {
		            tm.setTtCode(row.getString("TASK_TYPE_CODE"));
		        }
		        if (columnExists(row, "TT_DESC")) {
		            tm.setTtDesc(row.getString("TT_DESC"));
		        }
		        if (columnExists(row, "TASK_CATEGORY_CODE")) {
		            tm.setTcCode(row.getString("TASK_CATEGORY_CODE"));
		        }
		        if (columnExists(row, "TC_DESC")) {
		            tm.setTcDesc(row.getString("TC_DESC"));
		        }
		        if (columnExists(row, "REQUIREMENT_FROM")) {
		            tm.setRequirementFrom(row.getString("REQUIREMENT_FROM"));
		        }
		        if (columnExists(row, "QTY")) {
		            tm.setQty(row.getString("QTY"));
		        }
		        if (columnExists(row, "PROJECT_NAME")) {
		            tm.setProjectName(row.getString("PROJECT_NAME"));
		        }
		        if (columnExists(row, "CUSTOMER_NAME")) {
		            tm.setCustomerName(row.getString("CUSTOMER_NAME"));
		        }
		        if (columnExists(row, "PROJECT_CODE")) {
		            tm.setProjectCode(row.getString("PROJECT_CODE"));
		        }
		        if (columnExists(row, "ACTUAL__START_DATE")) {
		            tm.setActualStartDate(row.getString("ACTUAL__START_DATE"));
		        }
		} catch (Exception e) {
			logger.error("GetTaskEntryDtlRowMapper Exception--->" + e);
		}
		return tm;
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
