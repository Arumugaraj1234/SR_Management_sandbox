package com.vmfg.design.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.vmfg.design.entity.getPoDetailByIndentDtlEntity;

public class getPoDetailByIndentDtlRowMapper implements RowMapper<getPoDetailByIndentDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(getPoDetailByIndentDtlRowMapper.class);

	@Override
	public getPoDetailByIndentDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

		getPoDetailByIndentDtlEntity res = new getPoDetailByIndentDtlEntity();
		try {
			res.setPoCode(rs.getString("PO_CODE"));
			res.setVendorName(rs.getString("VENDOR_NAME"));
			res.setUniteRate(rs.getString("UNITE_RATE"));
			res.setQty(rs.getString("Qty"));
			res.setTotalValue(rs.getString("TOTAL_VALUE"));
			res.setTenantId(rs.getString("TENANT_ID"));
			res.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			res.setDate(rs.getString("DATE"));
		} catch (Exception ex) {
			logger.error("getPoDetailByIndentDtlRowMapper  Method Exception" + ex);
		}
		return res;
	}

}
