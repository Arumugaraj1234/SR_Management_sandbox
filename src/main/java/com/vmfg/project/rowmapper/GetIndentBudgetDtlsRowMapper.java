package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.GetIndentBudgetDtlsEntity;

public class GetIndentBudgetDtlsRowMapper implements RowMapper<GetIndentBudgetDtlsEntity>{
	private static final Logger logger = LoggerFactory.getLogger(GetIndentBudgetDtlsRowMapper.class);
	@Override
	public GetIndentBudgetDtlsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetIndentBudgetDtlsEntity getIndentBudget = new GetIndentBudgetDtlsEntity();
		try {
			if (columnExists(rs, "INDENT_CODE")) {
				getIndentBudget.setIndentCode(rs.getString("INDENT_CODE"));
			}
			if (columnExists(rs, "CREATED_DATE")) {
				getIndentBudget.setCreatedDate(rs.getString("CREATED_DATE"));
			}
			if (columnExists(rs, "PSK_DESC")) {
				getIndentBudget.setPskDesc(rs.getString("PSK_DESC"));
			}
			if (columnExists(rs, "PK_DESC")) {
				getIndentBudget.setPkDesc(rs.getString("PK_DESC"));
			}

		} catch (Exception ex) {
			logger.error("GetIndentBudgetDtlsRowMapper  Method Exception" + ex);

		}
		return getIndentBudget;
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
