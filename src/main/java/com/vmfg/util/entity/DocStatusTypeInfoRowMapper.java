package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class DocStatusTypeInfoRowMapper implements RowMapper<DocStatusTypeInfo>{
	private static final Logger logger = LoggerFactory.getLogger(DocStatusTypeInfoRowMapper.class);
	@Override
	public DocStatusTypeInfo mapRow(ResultSet row, int rowNum) throws SQLException {
		DocStatusTypeInfo ds = new DocStatusTypeInfo();
		try {
			ds.setApprovingDeptCode(row.getString("APPROVING_DEPT_CODE"));
			ds.setApprovingDesCode(row.getString("APPROVING_DESIGNATION_CODE"));
			ds.setDocSequence(row.getString("APPROVING_DESIGNATION_CODE"));
			ds.setDocStatusCode(row.getString("DOCUMENT_STATUS_TYPE_CODE"));
			ds.setDocStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_CODE"));
		}catch(Exception ex) {
			logger.error("DocStatusTypeInfoRowMapper map row exception :"+ex);
		}
		return ds;
	}

}
