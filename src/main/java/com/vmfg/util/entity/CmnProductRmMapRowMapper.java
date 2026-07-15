package com.vmfg.util.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class CmnProductRmMapRowMapper implements RowMapper<ProductRmMap> {
	private static final Logger logger = LoggerFactory.getLogger(CmnProductRmMapRowMapper.class);
	@Override
	public ProductRmMap mapRow(ResultSet row, int rowNum) throws SQLException {
		ProductRmMap fi = new ProductRmMap();
		try {
			fi.setRmproductcode(row.getString("RM_SHEET_PRODUCT_CODE"));
			fi.setPartperstrip(row.getInt("PARTS_PER_STRIP"));
		}catch(Exception ex) {
			logger.error("CmnProductRmMapRowMapper map row exception -->"+ex);
		}
		return fi;
	}
}
