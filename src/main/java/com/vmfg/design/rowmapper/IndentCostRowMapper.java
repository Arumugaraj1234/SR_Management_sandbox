package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.IndentCostDtlEntity;

public class IndentCostRowMapper implements RowMapper<IndentCostDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentCostRowMapper.class);

	@Override
	public IndentCostDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentCostDtlEntity res = new IndentCostDtlEntity();
		try {
			res.setIndentId(rs.getString("INDENT_ID"));
			res.setIndentTypeCode(rs.getString("INDENT_TYPE_CODE"));
			res.setPkaId(rs.getString("PKA_ID"));
			res.setPksaId(rs.getString("PKSA_ID"));
			res.setCreatedDate(rs.getString("CREATED_DATE"));
			res.setBudgetValue(rs.getString("BUDGET_VALUE"));
			res.setTargetValue(rs.getString("TARGET_VALUE"));
			res.setScmBudgetAllocated(rs.getString("SCM_BUDGET_ALLOCATED"));
			res.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
			res.setBudgetStatus(rs.getString("Budget_Status"));
			res.setPkDesc(rs.getString("PK_DESC"));
			res.setPskDesc(rs.getString("PSK_DESC"));
			res.setSbcCode(rs.getString("SBC_CODE"));
			res.setSbcDesc(rs.getString("SBC_DESC"));
			res.setIndentCode(rs.getString("INDENT_CODE"));
			res.setCloseDate(rs.getString("CLOSED_DATE"));
			if (columnExists(rs, "DN_VALUE")) {
				res.setDnValue(rs.getString("DN_VALUE"));
			}
			
		}catch (Exception ex){
			logger.error("IndentCostRowMapper Method Exception" + ex);
		}
		return res;
		
	}

	//column checking purpose (column is there or not)
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
