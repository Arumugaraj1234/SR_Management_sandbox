package com.vmfg.project.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.ProjectWBSTemplate;

public class ProjectWBSTempRowMapper implements RowMapper<ProjectWBSTemplate> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectWBSTempRowMapper.class);

	@Override
	public ProjectWBSTemplate mapRow(ResultSet row, int rowNum) throws SQLException {
		ProjectWBSTemplate ph = new ProjectWBSTemplate();
		try {
			if (columnExists(row, "MILESTONE_NAME")) {
				ph.setMilestoneName(row.getString("MILESTONE_NAME"));
			}

			if (columnExists(row, "PM_TEMP_ID")) {
				ph.setPmTempId(row.getString("PM_TEMP_ID"));
			}

			if (columnExists(row, "RESPONSIBLE_DEPT_CODE")) {
				ph.setResponsibleDeptCode(row.getString("RESPONSIBLE_DEPT_CODE"));
			}

			if (columnExists(row, "RESPONSIBLE_USER")) {
				ph.setRespUser(row.getString("RESPONSIBLE_USER"));
			}

			if (columnExists(row, "TEMPLATE_NAME")) {
				ph.setTempName(row.getString("TEMPLATE_NAME"));
			}

			if (columnExists(row, "TENANT_ID")) {
				ph.setTenantId(row.getString("TENANT_ID"));
			}

			if (columnExists(row, "EMPLOYEE_FIRSTNAME")) {
				ph.setEmpName(row.getString("EMPLOYEE_FIRSTNAME"));
			}

			if (columnExists(row, "DEPARTMENT_NAME")) {
				ph.setDeptName(row.getString("DEPARTMENT_NAME"));
			}

		} catch (Exception e) {
			logger.error("ProjectWBSTempRowMapper Exception--->" + e);
		}
		return ph;
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
