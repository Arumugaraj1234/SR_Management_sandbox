package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.getAssyTaskReportEntity;

public class AssyTaskReportRowMapper implements RowMapper<getAssyTaskReportEntity> {
	private static final Logger logger = LoggerFactory.getLogger(AssyTaskReportRowMapper.class);

	@Override
	public getAssyTaskReportEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		getAssyTaskReportEntity lst = new getAssyTaskReportEntity();
		try {
			lst.setProjId(rs.getString("PM_HDR_ID"));
			lst.setPendingTask(rs.getString("PENDING_TASK"));
			lst.setOpenTask(rs.getString("OPEN_TASK"));
			lst.setCompletedTask(rs.getString("COMPLETED_TASK"));
			lst.setCompletedPer(rs.getString("COMPLETED_PERCENTAGE"));
			lst.setProjCode(rs.getString("PROJECT_CODE"));
		}catch(Exception ex) {
			logger.error("AssyTaskReportRowMapper Method Exception" + ex);
		}
		return lst;
	}
}
