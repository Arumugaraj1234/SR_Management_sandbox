package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.EmpStatusEntity;

public class EmpStatusRowMapper implements RowMapper<EmpStatusEntity> {
	private static final Logger logger = LoggerFactory.getLogger(EmpStatusRowMapper.class);

	@Override
	public EmpStatusEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		EmpStatusEntity departmentInfoEntity = new EmpStatusEntity();
		try {
			departmentInfoEntity.setStatusCode(rs.getString("EMPLOYMENT_STATUS_CODE"));
			departmentInfoEntity.setStatusName(rs.getString("EMPLOYEE_STATUS_NAME"));
		} catch (Exception ex) {
			logger.error("EmpDesigRowMapper  Method Exception" + ex);

		}
		return departmentInfoEntity;
	}

}
