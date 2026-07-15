package com.vmfg.project.rowmapper;

import com.vmfg.project.response.ProjectInternalResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectInternalRowMapper implements RowMapper<ProjectInternalResponse> {
    @Override
    public ProjectInternalResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProjectInternalResponse response = new ProjectInternalResponse();
        response.setIsInternal(rs.getString("IS_INTERNAL"));
        return response;
    }
}
