package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.ProjectSubAreaExtnEntity;

public class ProjectSubAreaExtnRowMapper implements RowMapper<ProjectSubAreaExtnEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectSubAreaExtnRowMapper.class);
	@Override
	public ProjectSubAreaExtnEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		ProjectSubAreaExtnEntity ph = new ProjectSubAreaExtnEntity();
		try {
			ph.setAllocatedQty(row.getString("ALLOCATED_QTY"));
			ph.setAllocateVal(row.getString("ALLOCATED_VALUE"));
			ph.setPkseId(row.getString("PKSE_ID"));
			ph.setPkaId(row.getString("PKA_ID"));
			ph.setSbExtnId(row.getString("SB_EXTN_ID"));
			ph.setBudgetQty(row.getString("BUDGET_QTY"));
			ph.setBudgetValue(row.getString("BUDGET_VALUE"));
		} catch (Exception e) {
			logger.error("ProjectSubAreaExtnRowMapper Exception--->" + e);
		}
		return ph;
	}

}
