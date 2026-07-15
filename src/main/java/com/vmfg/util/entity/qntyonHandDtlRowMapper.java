package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class qntyonHandDtlRowMapper implements RowMapper<qntyonHandDtlentity> {
	private static final Logger logger = LoggerFactory.getLogger(qntyonHandDtlRowMapper.class);
	@Override
	public qntyonHandDtlentity mapRow(ResultSet row, int rowNum) throws SQLException {
		qntyonHandDtlentity fi = new qntyonHandDtlentity();
		try {
			fi.setInvtryprdctdtlid(row.getInt("INVENTORY_PRODUCT_DTL_ID"));
			fi.setInvtryprdctquantityonhand(row.getInt("PRODUCT_QUANTITY_ON_HAND"));
			fi.setInvtrylocationcode(row.getString("INVENTORY_LOCATION_CODE"));
		}catch(Exception ex) {
			logger.error("qntyonHandDtlRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
