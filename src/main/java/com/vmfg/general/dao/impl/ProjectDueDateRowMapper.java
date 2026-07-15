package com.vmfg.general.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.general.entity.ProjectDueDateEntity;

public class ProjectDueDateRowMapper implements RowMapper<ProjectDueDateEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ProjectDueDateRowMapper.class);

	@Override
	public ProjectDueDateEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		ProjectDueDateEntity pd = new ProjectDueDateEntity();
		try {
		    pd.setPdId(rs.getString("PD_ID"));
            pd.setPmHdrId(rs.getString("PM_HDR_ID"));
            pd.setDueDate(rs.getString("DUE_DATE"));
            pd.setReason(rs.getString("REASON"));
            pd.setUpdatedBy(rs.getString("UPDATED_BY"));
            pd.setUpdatedOn(rs.getString("UPDATED_ON"));
            pd.setTenantId(rs.getString("TENANT_ID"));
			
		} catch (Exception ex) {
			logger.error("ProjectDueDateRowMapper  Method Exception" + ex);

		}
		return pd;
	}

}