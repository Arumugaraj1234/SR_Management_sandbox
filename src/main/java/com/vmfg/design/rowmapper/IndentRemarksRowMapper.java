package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.design.entity.IndentRemarksEntity;

public class IndentRemarksRowMapper implements RowMapper<IndentRemarksEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentRemarksRowMapper.class);

	@Override
	public IndentRemarksEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentRemarksEntity res = new IndentRemarksEntity();
		try {
			res.setRemarks(rs.getString("REMARKS"));
			res.setStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			res.setUpdatedBy(rs.getString("EMPLOYEE_FIRSTNAME"));
			res.setUpdatedOn(rs.getString("UPDATED_ON"));
		} catch (Exception ex) {
			logger.error("KeyArea  Method Exception" + ex);
		}
		return res;
	}

}
