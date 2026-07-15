package com.vmfg.mis.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.mis.entity.InitialScopValuePOEntity;

public class InitialScopePoValueEntityRowMapper implements RowMapper<InitialScopValuePOEntity> {
	private static final Logger logger = LoggerFactory.getLogger(InitialScopePoValueEntityRowMapper.class);

	@Override
	public InitialScopValuePOEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		InitialScopValuePOEntity lst = new InitialScopValuePOEntity();
		try {
			lst.setIndentBasicTotal(rs.getString("UNIT_INI_BASIC_TOTAL"));
			lst.setPoNumber(rs.getString("PO_CODE"));
			lst.setTotalValue(rs.getString("TOTAL_VALUE"));
			lst.setVendorName(rs.getString("VENDOR_NAME"));
			lst.setIndentCode(rs.getString("INDENT_CODE"));
			lst.setIndentFinalTotal(rs.getString("EXTN_INI_BASIC_TOTAL"));
			lst.setPoType(rs.getString("PO_TYPE"));
			lst.setDiffrence(rs.getString("diffrence"));
			lst.setPoDate(rs.getString("DATE"));
			lst.setDeliveryLocation(rs.getString("DELIVERY_NAME"));
			lst.setProjCode(rs.getString("PROJECT_CODE"));
			lst.setStation(rs.getString("PK_DESC"));
	        lst.setSubAssy(rs.getString("PSK_DESC"));
	        lst.setIndentTypeDesc(rs.getString("INDENT_TYPE_DESC"));
		}catch(Exception ex) {
			logger.error("InitialScopePoValueEntityRowMapper Method Exception" + ex);
		}
		return lst;
	}

	

}
