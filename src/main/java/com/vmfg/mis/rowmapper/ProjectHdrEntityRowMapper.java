package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.ProjectHdrEntity;

public class ProjectHdrEntityRowMapper implements RowMapper<ProjectHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(AssyTaskReportRowMapper.class);

	@Override
	public ProjectHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProjectHdrEntity lst = new ProjectHdrEntity();
		try {
			lst.setCompletedDateTime(rs.getString("COMPLETED_DATETIME"));
			lst.setCreatedDate(rs.getString("CREATED_DATE"));
			lst.setEnquiryId(rs.getString("ENQUIRY_ID"));
			lst.setIsCompleted(rs.getString("IS_COMPLETED"));
			lst.setLastUpdatedDateTime(rs.getString("LAST_UPDATED_DATETIME"));
			lst.setPmHdrId(rs.getString("PM_HDR_ID"));
			lst.setProjectName(rs.getString("PROJECT_NAME"));
			lst.setTenantId(rs.getString("TENANT_ID"));
		}catch(Exception ex) {
			logger.error("ProjectHdrEntityRowMapper Method Exception" + ex);
		}
		return lst;
	}

	

}
