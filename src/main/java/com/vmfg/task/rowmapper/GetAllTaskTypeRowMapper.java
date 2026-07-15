package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetAllTaskTypeEntity;

public class GetAllTaskTypeRowMapper implements RowMapper<GetAllTaskTypeEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetAllTaskTypeRowMapper.class);

	@Override
	public GetAllTaskTypeEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetAllTaskTypeEntity tm = new GetAllTaskTypeEntity();
		try {
			tm.setIsActive(row.getString("IS_ACTIVE"));
			tm.setDeptCode(row.getString("DEPT_CODE"));
			tm.setDeptName(row.getString("DEPARTMENT_NAME"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTtCode(row.getString("TT_CODE"));
			tm.setTtDesc(row.getString("TT_DESC"));
		} catch (Exception e) {
			logger.error("GetAllTaskTypeRowMapper Exception--->" + e);
		}
		return tm;
	}


}
