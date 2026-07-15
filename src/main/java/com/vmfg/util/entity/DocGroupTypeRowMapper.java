package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class DocGroupTypeRowMapper implements RowMapper<DocGroupTypeEntity> {
	private static final Logger logger = LoggerFactory.getLogger(DocGroupTypeRowMapper.class);

	@Override
	public DocGroupTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		DocGroupTypeEntity result=new DocGroupTypeEntity();
		try {
			result.setDocGroupCode(rs.getString("DOC_GROUP"));
			result.setDocGroupDesc(rs.getString("INDENT_TYPE_DESC"));
		}catch(Exception ex) {
			logger.error("DocGroupTypeRowMapper error "+ex);
		}
		return result;
	}


}
