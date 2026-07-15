package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.BudgetExcessAssSubAssEntity;

public class BudgetExcessAssSubAssRowMapper implements RowMapper<BudgetExcessAssSubAssEntity>{
	private static final Logger logger = LoggerFactory.getLogger(BudgetExcessAssSubAssRowMapper.class);
	@Override
	public BudgetExcessAssSubAssEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		BudgetExcessAssSubAssEntity getIndentBudget = new BudgetExcessAssSubAssEntity();
		try {
			getIndentBudget.setDesc(rs.getString("Des"));
			getIndentBudget.setId(rs.getString("id"));
		} catch (Exception ex) {
			logger.error("BudgetExcessAssSubAssRowMapper  Method Exception" + ex);
		}
		return getIndentBudget;
	}
}
