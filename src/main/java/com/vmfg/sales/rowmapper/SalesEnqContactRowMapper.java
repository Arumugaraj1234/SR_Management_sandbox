package com.vmfg.sales.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.sales.entity.SalesEnqContactEntity;

public class SalesEnqContactRowMapper implements RowMapper<SalesEnqContactEntity>{
	private static final Logger logger = LoggerFactory.getLogger(SalesEnqContactRowMapper.class);
	@Override
	public SalesEnqContactEntity mapRow(ResultSet row, int rowNum) throws SQLException {
		SalesEnqContactEntity se = new SalesEnqContactEntity();
		try {
		
			se.setContactEmail(row.getString("CONTACT_EMAIL"));
			se.setContactName(row.getString("CONTACT_NAME"));
			se.setContactNo(row.getString("CONTACT_NO"));
			se.setMasterId(row.getString("SEC_ID"));
			se.setSecId(row.getString("SEC_ID"));
			se.setDepartment(row.getString("DEPARTMENT"));
			if(row.getString("IS_PRIMARY").equalsIgnoreCase("1")) {
				se.setPrimary(true);
			}else {
				se.setPrimary(false);
			}
			
		} catch (Exception e) {
			logger.error("SalesEnqContactRowMapper Method Exception---->"+e);
		}
		return se;
	}


}
