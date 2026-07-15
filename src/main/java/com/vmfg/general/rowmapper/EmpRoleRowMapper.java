package com.vmfg.general.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.EmpRoleEntity;

public class EmpRoleRowMapper implements RowMapper<EmpRoleEntity> {
	private static final Logger logger = LoggerFactory.getLogger(EmpRoleRowMapper.class);

	@Override
	public EmpRoleEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		EmpRoleEntity departmentInfoEntity = new EmpRoleEntity();
		try {
			departmentInfoEntity.setRoleCode(rs.getString("USER_ROLE_ID"));
			departmentInfoEntity.setRoleName(rs.getString("ROLE_NAME"));

		} catch (Exception ex) {
			logger.error("EmpRoleRowMapper  Method Exception" + ex);

		}
		return departmentInfoEntity;
	}

}
