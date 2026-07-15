package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class RejectionHdrDtlRowMapper implements RowMapper<RejectionHdrDtl> {
	private static final Logger logger = LoggerFactory.getLogger(RejectionHdrDtlRowMapper.class);

	@Override
	public RejectionHdrDtl mapRow(ResultSet row, int rowNum) throws SQLException {
		RejectionHdrDtl qi = new RejectionHdrDtl();
		try {
			qi.setProductCode(row.getString("PRODUCT_CODE"));
			qi.setBranchCode(row.getString("BRANCH_CODE"));
			qi.setProgramCode(row.getString("PROGRAM_CODE"));
		} catch (Exception ex) {
			logger.error("RejectionHdrDtlRowMapper map row exception :" + ex);
		}
		return qi;
	}

}
