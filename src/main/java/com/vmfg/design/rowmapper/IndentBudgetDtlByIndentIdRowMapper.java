package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.IndentBudgetDtlByIndentIdEntity;

public class IndentBudgetDtlByIndentIdRowMapper  implements RowMapper<IndentBudgetDtlByIndentIdEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentBudgetDtlByIndentIdRowMapper.class);

	@Override
	public IndentBudgetDtlByIndentIdEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentBudgetDtlByIndentIdEntity res = new IndentBudgetDtlByIndentIdEntity();
		try {
			res.setBudgetValue(rs.getString("BUDGET_VALUE"));
			res.setBudgetQty(rs.getString("BUDGET_QTY"));
			res.setIndentBudId(rs.getString("INDENT_BUD_ID"));
			res.setIndentId(rs.getString("INDENT_ID"));
			res.setPkaId(rs.getString("PKA_ID"));
			res.setSbExtnId(rs.getString("SB_EXTN_ID"));
			res.setTenantId(rs.getString("TENANT_ID"));
		} catch (Exception ex) {
			logger.error("IndentBudgetDtlRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
