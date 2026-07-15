package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.BudgetExcessSheetEntity;

public class RetriveBudgetExcessSheetDtlRowMapper implements RowMapper<BudgetExcessSheetEntity>{
	private static final Logger logger = LoggerFactory.getLogger(RetriveBudgetExcessSheetDtlRowMapper.class);
	@Override
	public BudgetExcessSheetEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		BudgetExcessSheetEntity budgetExcessSheetEntity = new BudgetExcessSheetEntity();
		try {
			
			if (columnExists(rs, "BE_HDR_ID")) {
				budgetExcessSheetEntity.setBeHdrId(rs.getString("BE_HDR_ID"));
			}
			if (columnExists(rs, "INDENT_ID")) {
				budgetExcessSheetEntity.setIndentId(rs.getString("INDENT_ID"));
			}
			if (columnExists(rs, "PM_HDR_ID")) {
				budgetExcessSheetEntity.setPmHdrId(rs.getString("PM_HDR_ID"));
			}
			
			if (columnExists(rs, "BUDGET_COST")) {
				budgetExcessSheetEntity.setBudgetCost(rs.getString("BUDGET_COST"));
			}
			if (columnExists(rs, "ACTUAL_COST")) {
				budgetExcessSheetEntity.setActualCost(rs.getString("ACTUAL_COST"));
			}
			if (columnExists(rs, "EXCESS")) {
				budgetExcessSheetEntity.setExcess(rs.getString("EXCESS"));
				
			}
			if (columnExists(rs, "VENDOR_CODE")) {
				budgetExcessSheetEntity.setVendorCode(rs.getString("VENDOR_CODE"));
			}
			if (columnExists(rs, "REASON")) {
				budgetExcessSheetEntity.setReason(rs.getString("REASON"));
			}
			if (columnExists(rs, "ROOT_CAUSE")) {
				budgetExcessSheetEntity.setRootCause(rs.getString("ROOT_CAUSE"));
			}
			if (columnExists(rs, "ACTION")) {
				budgetExcessSheetEntity.setAction(rs.getString("ACTION"));
			}
			if (columnExists(rs, "RESPONSIBLE")) {
				budgetExcessSheetEntity.setResponsible(rs.getString("RESPONSIBLE"));
			}
			if (columnExists(rs, "SEQUENCE_NO")) {
				budgetExcessSheetEntity.setSequenceNo(rs.getString("SEQUENCE_NO"));
			}

			if (columnExists(rs, "SEQUENCE_STATUS")) {
				budgetExcessSheetEntity.setSequenceStatus(rs.getString("SEQUENCE_STATUS"));
			}
			if (columnExists(rs, "UPDATED_BY")) {
				budgetExcessSheetEntity.setUpdatedBy(rs.getString("UPDATED_BY"));
			}
			if (columnExists(rs, "UPDATED_ON")) {
				budgetExcessSheetEntity.setUpdatedOn(rs.getString("UPDATED_ON"));
			}
			if (columnExists(rs, "TENANT_ID")) {
				budgetExcessSheetEntity.setTenantId(rs.getString("TENANT_ID"));
			}
			if (columnExists(rs, "DSK_ID")) {
				budgetExcessSheetEntity.setDskId(rs.getString("DSK_ID"));
			}
		
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				budgetExcessSheetEntity.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}
			if (columnExists(rs, "INDENT_CODE")) {
				budgetExcessSheetEntity.setIndentCode(rs.getString("INDENT_CODE"));
			}
			if (columnExists(rs, "PROJECT_CODE")) {
				budgetExcessSheetEntity.setProjectCode(rs.getString("PROJECT_CODE"));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
				budgetExcessSheetEntity.setProjectName(rs.getString("PROJECT_NAME"));
			}
			
			if (columnExists(rs, "VENDOR_NAME")) {
				budgetExcessSheetEntity.setVendorName(rs.getString("VENDOR_NAME"));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				budgetExcessSheetEntity.setStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			}
			if (columnExists(rs, "DEPARTMENT_NAME")) {
				budgetExcessSheetEntity.setResponsibleDesc(rs.getString("DEPARTMENT_NAME"));
			}
			if(columnExists(rs, "IS_COMPLETED")) {
				budgetExcessSheetEntity.setIsCompleted(rs.getString("IS_COMPLETED"));
			}
			if(columnExists(rs, "ACTUAL_EXCESS")) {
				budgetExcessSheetEntity.setActualExcess(rs.getString("ACTUAL_EXCESS"));
			}
			
		} catch (Exception ex) {
			logger.error("BudgetExcessSheetRowMapper  Method Exception" + ex);

		}
		return budgetExcessSheetEntity;
	}

	// column checking purpose (column is there or not)
	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
				return true;
			}
		}

		return false;
	}

}


