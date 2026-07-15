package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class ILUOEmplInfoRowMapper implements RowMapper<ILUOEmplInfo>{
	private static final Logger logger = LoggerFactory.getLogger(ILUOEmplInfoRowMapper.class);
	@Override
	public ILUOEmplInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		ILUOEmplInfo qa = new ILUOEmplInfo();
		try {
			qa.setTrainingBy(row.getString("TRAINING_BY"));
			qa.setTrainingByName(row.getString("TRAINING_BY_NAME"));
		
			qa.setCheckedBy(row.getString("CHECKED_BY"));
			qa.setCheckedByName(row.getString("CHECKED_BY_NAME"));
			
			qa.setApprovedBy(row.getString("APPROVED_BY"));
			qa.setApprovedByName(row.getString("APPROVED_BY_NAME"));
			
		}catch(Exception ex) {
			logger.error("ILUOEmplInfoRowMapper map row exception -->"+ex);
		}
		return qa;
	}
}
