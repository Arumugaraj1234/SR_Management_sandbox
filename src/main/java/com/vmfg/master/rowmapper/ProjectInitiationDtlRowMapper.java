package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.ProjectInitiationDtlEntity;

public class ProjectInitiationDtlRowMapper implements RowMapper<ProjectInitiationDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectInitiationDtlRowMapper.class);

	@Override
	public ProjectInitiationDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjectInitiationDtlEntity vendorInspection = new ProjectInitiationDtlEntity();
		try {
			vendorInspection.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
			vendorInspection.setDeptCode(rs.getString("DEPARTMENT_CODE"));
			vendorInspection.setMasterPoc(rs.getString("MASTER_POC"));
			vendorInspection.setPiId(rs.getString("PI_ID"));
			vendorInspection.setPrimaryPoc(rs.getString("PRIMARY_POC"));
			vendorInspection.setDepartmentAssigned(rs.getString("DEPARTMENT_ASSIGNED"));
		} catch (Exception ex) {
			logger.error("ProjectInitiationDtlRowMapper  Method Exception" + ex);
		}
		return vendorInspection;
	}
}