package com.vmfg.inventory.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmfg.scm.entity.ProjectDtlsEntity;

public class ProjectDrpDownRowMapper implements RowMapper<ProjectDtlsEntity>{
	private static final Logger logger = LoggerFactory.getLogger(ProjectDrpDownRowMapper.class);

	@Override
	public ProjectDtlsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		ProjectDtlsEntity pd = new ProjectDtlsEntity();
		try {
			pd.setCustomerName(rs.getString("CUSTOMER_NAME"));
			pd.setProjectCode(rs.getString("PROJECT_CODE"));
			pd.setProjectId(rs.getString("PM_HDR_ID"));
			pd.setProjectName(rs.getString("PROJECT_NAME"));
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ProjectDrpDownRowMapper  Method Exception" + e);
		}
		return pd;
	}

}
