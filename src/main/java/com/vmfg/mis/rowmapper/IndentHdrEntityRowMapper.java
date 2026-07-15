package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.IndentHdrEntity;

public class IndentHdrEntityRowMapper implements RowMapper<IndentHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentHdrEntityRowMapper.class);

	@Override
	public IndentHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentHdrEntity lst = new IndentHdrEntity();
		try {
			lst.setBudgetAllocated(rs.getString("SCM_BUDGET_ALLOCATED"));
			lst.setBudgetValue(rs.getString("BUDGET_VALUE"));
			lst.setSbcCode(rs.getString("SBC_CODE"));
			lst.setTargetValue(rs.getString("TARGET_VALUE"));
		}catch(Exception ex) {
			logger.error("IndentHdrEntityRowMapper Method Exception" + ex);
		}
		return lst;
	}

	

}
