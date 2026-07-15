package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.response.BudgetExcessBasedIndentHdrDtl;

public class BudgetExcessSheetRowMapper implements RowMapper<BudgetExcessBasedIndentHdrDtl>{
	private static final Logger logger = LoggerFactory.getLogger(BudgetExcessSheetRowMapper.class);
	@Override
	public BudgetExcessBasedIndentHdrDtl mapRow(ResultSet rs, int rowNum) throws SQLException {
		BudgetExcessBasedIndentHdrDtl budgetExcessBasedIndentHdrDtl = new BudgetExcessBasedIndentHdrDtl();
		try {
			if (columnExists(rs, "INDENT_ID")) {
				budgetExcessBasedIndentHdrDtl.setIndentId(rs.getString("INDENT_ID"));
			}
			if (columnExists(rs, "PROJECT_ID")) {
				budgetExcessBasedIndentHdrDtl.setPmHdrId(rs.getString("PROJECT_ID"));
			}
			if (columnExists(rs, "TARGET_VALUE")) {
				budgetExcessBasedIndentHdrDtl.setTargetValue(rs.getString("TARGET_VALUE"));
			}
			if (columnExists(rs, "SCM_BUDGET_ALLOCATED")) {
				budgetExcessBasedIndentHdrDtl.setScmBudAllocatedValue(rs.getString("SCM_BUDGET_ALLOCATED"));
			}
			if (columnExists(rs, "PKSA_ID")) {
				budgetExcessBasedIndentHdrDtl.setDskId(rs.getString("PKSA_ID"));
			}



		} catch (Exception ex) {
			logger.error("BudgetExcessSheetRowMapper  Method Exception" + ex);

		}
		return budgetExcessBasedIndentHdrDtl;
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