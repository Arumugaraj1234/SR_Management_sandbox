package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.TaskCompTimeEntity;

public class TaskCompTimeRowMapper implements RowMapper<TaskCompTimeEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DesignWidgetDtlMisRowMapper.class);

	@Override
	public TaskCompTimeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		TaskCompTimeEntity lst = new TaskCompTimeEntity();
		try {
			lst.setActivity(rs.getString("ACTIVITY_NAME"));
			lst.setCompPer(rs.getString("COMPLETED_PTG"));
			lst.setDelay(rs.getString("DELAY"));
			lst.setEndDate(rs.getString("PLANNED_COMPLETED_DATE"));
			lst.setProjName(rs.getString("PROJECT_NAME"));
			lst.setStartDate(rs.getString("PLANNED_START_DATE"));
			lst.setActualDate(rs.getString("ACTUAL__START_DATE"));
			lst.setCompletedDate(rs.getString("COMPLETED_DATE"));
		}catch(Exception ex) {
			logger.error("TaskCompTimeRowMapper  Method Exception" + ex);
		}
		return lst;
	}
}
