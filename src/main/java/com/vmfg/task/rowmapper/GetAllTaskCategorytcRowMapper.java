package com.vmfg.task.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.task.entity.GetAllTaskCategorytcEntity;

public class GetAllTaskCategorytcRowMapper implements RowMapper<GetAllTaskCategorytcEntity> {
	private static final Logger logger = LoggerFactory.getLogger(TaskEntryDtlRowMapper.class);

	@Override
	public GetAllTaskCategorytcEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		GetAllTaskCategorytcEntity tm = new GetAllTaskCategorytcEntity();
		try {
			tm.setIsActive(row.getString("IS_ACTIVE"));
			tm.setTcCode(row.getString("TC_CODE"));
			tm.setTcDesc(row.getString("TC_DESC"));
			tm.setTenantId(row.getString("TENANT_ID"));
			tm.setTtCode(row.getString("TT_CODE"));
			tm.setTtDesc(row.getString("TT_DESC"));
		} catch (Exception e) {
			logger.error("GetAllTaskCategorytcRowMapper Exception--->" + e);
		}
		return tm;
	}


}
