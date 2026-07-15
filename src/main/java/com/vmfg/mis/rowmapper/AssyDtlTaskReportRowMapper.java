package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.getAssyDtlTaskReportEntity;

public class AssyDtlTaskReportRowMapper implements RowMapper<getAssyDtlTaskReportEntity> {
	private static final Logger logger = LoggerFactory.getLogger(AssyTaskReportRowMapper.class);

	@Override
	public getAssyDtlTaskReportEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		getAssyDtlTaskReportEntity lst = new getAssyDtlTaskReportEntity();
		try {
			lst.setProjId(rs.getString("PM_HDR_ID"));
			lst.setOpenTask(rs.getString("OPEN_TASK"));
			lst.setPendingTask(rs.getString("PENDING_TASK"));
			lst.setDelayTask(rs.getString("DELAY_TASK"));
			lst.setCompPer(rs.getString("COMPLETED_PERCENTAGE"));
			lst.setProjCode(rs.getString("PROJECT_CODE"));
		}catch(Exception ex) {
			logger.error("AssyDtlTaskReportRowMapper Method Exception" + ex);
		}
		return lst;
	}

	

}
