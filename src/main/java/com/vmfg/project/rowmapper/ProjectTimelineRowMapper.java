package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.ProjectTimelineResp;

public class ProjectTimelineRowMapper implements RowMapper<ProjectTimelineResp> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectTimelineRowMapper.class);

	@Override
	public ProjectTimelineResp mapRow(ResultSet row, int rowNum) throws SQLException {
		ProjectTimelineResp ph = new ProjectTimelineResp();
		try {
			ph.setActualEndDate(row.getString("ACTUAL_END_DATE"));
			ph.setActualStartDate(row.getString("ACTUAL_START_DATE"));
			ph.setIsInitiated(row.getString("IS_INITIATED"));
			ph.setLastUpdatedDatetime(row.getString("LAST_UPDATED_DATETIME"));
			ph.setMilestoneName(row.getString("MILESTONE_NAME"));
			ph.setPlannedEndDate(row.getString("PLANNED_END_DATE"));
			ph.setPlannedStartDate(row.getString("PLANNED_START_DATE"));
			ph.setPmHdrId(row.getString("PM_HDR_ID"));
			ph.setPmTempId(row.getString("PM_TEMP_ID"));
			ph.setPtId(row.getString("PT_ID"));
			ph.setResponsibleDeptCode(row.getString("RESPONSIBLE_DEPT_CODE"));
			ph.setResponsibleName(row.getString("RESPONSIBLE_NAME"));
			ph.setTenantId(row.getString("TENANT_ID"));
			ph.setEmpName(row.getString("EMPLOYEE_FIRSTNAME"));
			ph.setDeptName(row.getString("DEPARTMENT_NAME"));
			ph.setGeneratedEndDate(row.getString("PLANNED_END_DATE"));
			ph.setGeneratedStartDate(row.getString("PLANNED_START_DATE"));
		} catch (Exception e) {
			logger.error("ProjectTimelineRowMapper Exception--->" + e);
		}
		return ph;
	}
}
