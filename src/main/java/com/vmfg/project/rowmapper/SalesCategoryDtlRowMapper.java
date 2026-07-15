package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.SalesCategoryDtlEntity;

public class SalesCategoryDtlRowMapper implements RowMapper<SalesCategoryDtlEntity>{
	private static final Logger logger = LoggerFactory.getLogger(SalesCategoryDtlRowMapper.class);
	@Override
	public SalesCategoryDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		SalesCategoryDtlEntity salesCategoryDtl = new SalesCategoryDtlEntity();
		try {
			if (columnExists(rs, "SBC_DESC")) {
				salesCategoryDtl.setCategoryDesc(rs.getString("SBC_DESC"));
			}
			if (columnExists(rs, "SBC_CODE")) {
				salesCategoryDtl.setSbcCode(rs.getString("SBC_CODE"));
			}
			if (columnExists(rs, "BUDGET_VALUE")) {
				salesCategoryDtl.setBudgetValue(rs.getString("BUDGET_VALUE"));
			}
			if (columnExists(rs, "SCM_BUDGET_ALLOCATED")) {
				salesCategoryDtl.setScmBudgetAllocated(rs.getString("SCM_BUDGET_ALLOCATED"));
			}

		} catch (Exception ex) {
			logger.error("SalesCategoryDtlRowMapper  Method Exception" + ex);

		}
		return salesCategoryDtl;
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
