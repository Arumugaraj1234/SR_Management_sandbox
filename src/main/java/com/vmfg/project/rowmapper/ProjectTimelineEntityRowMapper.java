package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.ProjectTimelineEntity;

public class ProjectTimelineEntityRowMapper implements RowMapper<ProjectTimelineEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectTimelineEntityRowMapper.class);

	@Override
	public ProjectTimelineEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjectTimelineEntity result = new ProjectTimelineEntity();
		try {

			if (columnExists(rs, "MILESTONE_NAME")) {
				result.setMilestoneName(rs.getString("MILESTONE_NAME"));
			}
			if (columnExists(rs, "PLANNED_START_DATE")) {
				result.setPlannedStartDate(rs.getString("PLANNED_START_DATE"));
			}
			if (columnExists(rs, "PLANNED_END_DATE")) {
				result.setPlannedEndDate(rs.getString("PLANNED_END_DATE"));
			}
			if (columnExists(rs, "EMPLOYEE_FIRSTNAME")) {
				result.setEmployeeName(rs.getString("EMPLOYEE_FIRSTNAME"));
			}
			if (columnExists(rs, "EMPLOYEE_ID")) {
				result.setEmployeeId(rs.getString("EMPLOYEE_ID"));
			}
			if (columnExists(rs, "DEPARTMENT_NAME")) {
				result.setDepartName(rs.getString("DEPARTMENT_NAME"));
			}
			if (columnExists(rs, "DEPARTMENT_CODE")) {
				result.setDepartmentCode(rs.getString("DEPARTMENT_CODE"));
			}

		} catch (Exception ex) {
			logger.error("ProjectTimelineEntityRowMapper error " + ex);
		}

		return result;
	}

	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}

}
