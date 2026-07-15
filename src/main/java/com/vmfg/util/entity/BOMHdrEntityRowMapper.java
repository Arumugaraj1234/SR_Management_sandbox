package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BOMHdrEntityRowMapper implements RowMapper<BOMHdrEntity>{

	@Override
	public BOMHdrEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		BOMHdrEntity bhdr = new BOMHdrEntity();
		try {
			bhdr.setBomhdrid(row.getInt("BOM_ID"));
			bhdr.setBomhdrpartcode(row.getString("PARENT_PART_CODE"));
			bhdr.setTenantid("TENANT_ID");
		} catch (Exception e) {
			
		}
		return bhdr;
	}

}
