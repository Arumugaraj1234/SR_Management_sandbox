package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.DesignationEntity;

public class EmpDesigRowMapper implements RowMapper<DesignationEntity> {
	private static final Logger logger = LoggerFactory.getLogger(EmpDesigRowMapper.class);

	@Override
	public DesignationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		DesignationEntity departmentInfoEntity = new DesignationEntity();
		try {
			departmentInfoEntity.setDesigCode(rs.getString("DESIGNATION_CODE"));
			departmentInfoEntity.setDesigName(rs.getString("DESIGNATION_NAME"));

		} catch (Exception ex) {
			logger.error("EmpDesigRowMapper  Method Exception" + ex);

		}
		return departmentInfoEntity;
	}

}
