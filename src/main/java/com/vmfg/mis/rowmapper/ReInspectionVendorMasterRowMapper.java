package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.project.entity.ReInspectionVendorMasterEntity;

public class ReInspectionVendorMasterRowMapper implements RowMapper<ReInspectionVendorMasterEntity> {
	private static final Logger logger = LoggerFactory.getLogger(ReInspectionVendorMasterRowMapper.class);

	@Override
	public ReInspectionVendorMasterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReInspectionVendorMasterEntity lst = new ReInspectionVendorMasterEntity();
		try {
			lst.setMessage(rs.getString("message"));
			lst.setTenantId(rs.getString("TENANT_ID"));
		}catch(Exception ex) {
			logger.error("ReInspectionVendorMasterRowMapper Method Exception" + ex);
		}
		return lst;
	}

	

}
