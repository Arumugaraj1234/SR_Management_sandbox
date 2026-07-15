package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.SumOfIndentHdrEntity;

public class SumOfIndentHdrRowMapper  implements RowMapper<SumOfIndentHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(SumOfIndentHdrRowMapper.class);

	@Override
	public SumOfIndentHdrEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		SumOfIndentHdrEntity ph = new SumOfIndentHdrEntity();
		try {
		
			
			ph.setBudgetCost(row.getString("BUDGET_VALUE"));
			ph.setTargetCost(row.getString("TARGET_VALUE"));
			ph.setActualCost(row.getString("SCM_BUDGET_ALLOCATED"));
		} catch (Exception e) {
			logger.error("SumOfIndentHdrRowMapper Exception--->" + e);
		}
		return ph;
	}

}
