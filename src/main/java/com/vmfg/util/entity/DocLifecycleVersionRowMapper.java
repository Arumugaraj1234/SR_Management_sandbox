package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class DocLifecycleVersionRowMapper implements RowMapper<DocLifecycleVersionEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocLifecycleVersionRowMapper.class);

	@Override
	public DocLifecycleVersionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DocLifecycleVersionEntity result=new DocLifecycleVersionEntity();
		try {
			result.setVersion(rs.getString("VERSION"));
			result.setVersiondatetime(rs.getString("VERSION_DATETIME"));
			result.setUpdatedBy(rs.getString("UPDATED_BY"));
		}catch(Exception ex) {
			logger.error("DocLifecycleVersionRowMapper error "+ex);
		}
		return result;
	}

} 
