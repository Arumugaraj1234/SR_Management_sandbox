package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.ProjectProgressEntity;

public class ProjectProgressRowMapper implements RowMapper<ProjectProgressEntity>{
	private static final Logger logger = LoggerFactory.getLogger(ProjectProgressRowMapper.class);

	@Override
	public ProjectProgressEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		ProjectProgressEntity result=new ProjectProgressEntity();
		try {
			result.setCompletedDate(rs.getString("COMPLETED_DATETIME"));
			result.setPlanEndDate(rs.getString("PLANNED_END_DATE"));
			result.setPlanStartDate(rs.getString("PLANNED_START_DATE"));
			result.setProjectCode(rs.getString("PROJECT_CODE"));
			result.setProjectName(rs.getString("PROJECT_NAME"));
			result.setCustomerName(rs.getString("CUSTOMER_NAME"));
			
		}catch(Exception ex) {
			logger.error("ProjectProgressRowMapper error "+ex);
		}
		return result;
	}

}
