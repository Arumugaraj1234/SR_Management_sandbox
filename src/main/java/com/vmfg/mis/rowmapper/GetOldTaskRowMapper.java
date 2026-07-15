package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.TaskDtlEntity;

public class GetOldTaskRowMapper implements RowMapper<TaskDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetOldTaskRowMapper.class);

	@Override
	public TaskDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TaskDtlEntity lst = new TaskDtlEntity();
		try {
			lst.setAssignedTo(rs.getString("ASSIGNED_TO"));
			lst.setCnt(rs.getString("cnt"));
			lst.setPmHdrid(rs.getString("PM_HDR_ID"));
			lst.setDeptCode(rs.getString("DEPARTMENT_CODE"));
		} catch (Exception ex) {
			logger.error("GetOldTaskRowMapper  Method Exception" + ex);
		}
		return lst;
	}
}
