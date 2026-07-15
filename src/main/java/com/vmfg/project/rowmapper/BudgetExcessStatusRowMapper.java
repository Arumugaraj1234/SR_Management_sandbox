package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.RetriveBudgetExcessStatusDtlEntity;

public class BudgetExcessStatusRowMapper implements RowMapper<RetriveBudgetExcessStatusDtlEntity>{
	private static final Logger logger = LoggerFactory.getLogger(BudgetExcessStatusRowMapper.class);
	@Override
	public RetriveBudgetExcessStatusDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		RetriveBudgetExcessStatusDtlEntity budgetExcessStatusEntity = new RetriveBudgetExcessStatusDtlEntity();
		try {

			if (columnExists(rs, "BES_ID")) {
				budgetExcessStatusEntity.setBesId(rs.getString("BES_ID"));
			}
			if (columnExists(rs, "BE_HDR_ID")) {
				budgetExcessStatusEntity.setBeHdrId(rs.getString("BE_HDR_ID"));
			}
			if (columnExists(rs, "SEQUENCE_NO")) {
				budgetExcessStatusEntity.setSequenceNo(rs.getString("SEQUENCE_NO"));
			}
			if (columnExists(rs, "SEQUENCE_STATUS")) {
				budgetExcessStatusEntity.setSequenceStatus(rs.getString("SEQUENCE_STATUS"));
			}
			if (columnExists(rs, "REMARKS")) {
				budgetExcessStatusEntity.setRemarks(rs.getString("REMARKS"));
			}
			if (columnExists(rs, "UPDATED_BY")) {
				budgetExcessStatusEntity.setUpdatedBy(rs.getString("UPDATED_BY"));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				budgetExcessStatusEntity.setEmpName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}
			if (columnExists(rs, "UPDATED_ON")) {
				budgetExcessStatusEntity.setUpdatedOn(rs.getString("UPDATED_ON"));
			}
			if (columnExists(rs, "TENANT_ID")) {
				budgetExcessStatusEntity.setTenantId(rs.getString("TENANT_ID"));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				budgetExcessStatusEntity.setSequenceStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			}


		} catch (Exception ex) {
			logger.error("BudgetExcessStatusRowMapper  Method Exception" + ex);

		}
		return budgetExcessStatusEntity;
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