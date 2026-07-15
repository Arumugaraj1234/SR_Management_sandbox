package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class EmployeeDesignationRowMapper implements RowMapper<EmployeeDesignationEntity> {
	private static final Logger logger = LoggerFactory.getLogger(EmployeeDesignationRowMapper.class);

	@Override
	public EmployeeDesignationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		EmployeeDesignationEntity result=new EmployeeDesignationEntity();
		// TODO Auto-generated method stub
		try {
			result.setDesignationCode(rs.getString("DESIGNATION_CODE"));
			result.setDesignationDesc(rs.getString("DESIGNATION_NAME"));

		}catch(Exception ex) {
			logger.error("EmployeeDesignationRowMapper error "+ex);
		}
		return result;
	}

}
