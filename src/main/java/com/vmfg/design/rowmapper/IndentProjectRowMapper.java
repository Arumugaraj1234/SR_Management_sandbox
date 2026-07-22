package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.IndentProjectEntity;

public class IndentProjectRowMapper implements RowMapper<IndentProjectEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentProjectRowMapper.class);

	@Override
	public IndentProjectEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentProjectEntity res = new IndentProjectEntity();
		try {
			res.setCreatedDate(rs.getString("CREATED_DATE"));
			res.setExpDeliveryDate(rs.getString("EXPECTED_DELIVERY_DATE"));
			res.setIndentCode(rs.getString("INDENT_CODE"));
			res.setProjectCode(rs.getString("PROJECT_CODE"));
			res.setProjectName(rs.getString("PROJECT_NAME"));
			res.setTotalBudgetConsumed(rs.getString("SCM_BUDGET_ALLOCATED"));
			res.setCostFlowType(rs.getString("COST_FLOW_TYPE"));
		} catch (Exception ex) {
			logger.error("IndentProjectRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
