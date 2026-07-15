
package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.VendorCategoryEntity;

public class VendorCategoryRowMapper implements RowMapper<VendorCategoryEntity> {
	private static final Logger logger = LoggerFactory.getLogger(VendorCategoryRowMapper.class);

	@Override
	public VendorCategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		VendorCategoryEntity res = new VendorCategoryEntity();
		try {
			res.setVendorCategory(rs.getString("VC"));;
			res.setVendorId(rs.getString("VC_ID"));
			res.setTenantId(rs.getString("TENANT_ID"));;			
		} catch (Exception ex) {
			logger.error("VendorCategoryRowMapper  Method Exception" + ex);
		}
		return res;
	}
}
