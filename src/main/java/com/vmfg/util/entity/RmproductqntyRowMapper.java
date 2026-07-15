package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class RmproductqntyRowMapper implements RowMapper<Rmproductqntyentity> {
	private static final Logger logger = LoggerFactory.getLogger(RmproductqntyRowMapper.class);
	@Override
	public Rmproductqntyentity mapRow(ResultSet row, int rowNum) throws SQLException {
		Rmproductqntyentity fi = new Rmproductqntyentity();
		try {
			fi.setRmproductcode(row.getString("RM_SHEET_PRODUCT_CODE"));
			fi.setProductquantity(row.getInt("Multiple"));
			fi.setWorkorderid(row.getInt("WORKORDER_ID"));
			fi.setWorkorderqunty(row.getInt("PRODUCT_QUANTITY"));
		}catch(Exception ex) {
			logger.error("locationinvntryRowMapper map row exception -->"+ex);
		}
		return fi;
	}

}
