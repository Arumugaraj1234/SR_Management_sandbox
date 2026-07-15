package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.ProjConsumedValEntity;

public class GetProjConsumedValRowMapper implements RowMapper<ProjConsumedValEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetProjConsumedValRowMapper.class);

	@Override
	public ProjConsumedValEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjConsumedValEntity lst = new ProjConsumedValEntity();
		try {
			lst.setBudgetConsumed(rs.getString("BUDGET_VALUE"));
			lst.setPoReleased(rs.getString("BASIC_TOTAL"));
			lst.setProjBudget(rs.getString("PROJECT_BUDGET"));
//			lst.setActualSpend(rs.getString("ACTUAL_SPENT"));
		}catch(Exception ex) {
			logger.error("GetProjConsumedValRowMapper  Method Exception" + ex);
		}
		return lst;
	}

}
