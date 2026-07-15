package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class OperatorTrainingPlanInfoRowMapper implements RowMapper<ILUOEntryInfo> {
	private static final Logger logger = LoggerFactory.getLogger(OperatorTrainingPlanInfoRowMapper.class);

	@Override
	public ILUOEntryInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		ILUOEntryInfo tm = new ILUOEntryInfo();
		try {
			tm.setOperatorName(row.getString("EMPLOYEE_FIRSTNAME"));
			tm.setProcess(row.getString("DESCRIPTION"));
			tm.setTrainingType(row.getString("TRAINING_TYPE_DESCRIPTION"));
		} catch (Exception e) {
			logger.error("OperatorTrainingPlanInfoRowMapper Exception--->" + e);
		}
		return tm;
	}
}
