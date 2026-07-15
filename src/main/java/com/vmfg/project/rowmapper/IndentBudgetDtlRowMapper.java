package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.IndentBudgetDtlEntity;


public class IndentBudgetDtlRowMapper implements RowMapper<IndentBudgetDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentBudgetDtlRowMapper.class);

	@Override
	public IndentBudgetDtlEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		IndentBudgetDtlEntity ph = new IndentBudgetDtlEntity();
		try {
			ph.setAllocatedQty(row.getString("ALLOCATED_QTY"));
			ph.setAllocatedVal(row.getString("ALLOCATED_VALUE"));
			ph.setIndentDtlId(row.getString("INDENT_DTL_ID"));
			ph.setIndentBudId(row.getString("INDENT_BUD_ID"));
			ph.setPkseId(row.getString("PKSE_ID"));
			
		} catch (Exception e) {
			logger.error("ProjectHdrRowMapper Exception--->" + e);
		}
		return ph;
	}

}
