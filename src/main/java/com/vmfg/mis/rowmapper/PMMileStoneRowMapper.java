package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.response.ReportProjectMilestoneResponse;

public class PMMileStoneRowMapper implements RowMapper<ReportProjectMilestoneResponse> {
	private static final Logger logger = LoggerFactory.getLogger(PMMileStoneRowMapper.class);

	@Override
	public ReportProjectMilestoneResponse mapRow(ResultSet row, int rowNum) throws SQLException {
		ReportProjectMilestoneResponse ph = new ReportProjectMilestoneResponse();
		try {
			ph.setDepartment(row.getString("DEPARTMENT_NAME"));
			ph.setMileStone(row.getString("MILESTONE_NAME"));
			ph.setPlannedEndDate(row.getString("PLANNED_END_DATE"));
			ph.setPlannedStartDate(row.getString("PLANNED_START_DATE"));
			ph.setProjectCode(row.getString("PROJECT_CODE"));
			ph.setDeptCode(row.getString("RESPONSIBLE_DEPT_CODE"));
			ph.setPmHdrId(row.getString("PM_HDR_ID"));

		} catch (Exception e) {
			logger.error("PMMileStoneRowMapper Exception--->" + e);
		}
		return ph;
	}

}
