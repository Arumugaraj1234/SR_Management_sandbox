package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.VendorTypeCateCountEntity;

public class VendorTypeCatRowMapper implements RowMapper<VendorTypeCateCountEntity> {
	private static final Logger logger = LoggerFactory.getLogger(VendorTypeCatRowMapper.class);
	
	@Override
	public VendorTypeCateCountEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		VendorTypeCateCountEntity lst = new VendorTypeCateCountEntity();
		try {
			lst.setCount(rs.getString("count"));
			lst.setDesc(rs.getString("description"));
		}catch(Exception ex) {
			logger.error("VendorTypeCatRowMapper Method Exception" + ex);
		}
		return lst;
	}
}
