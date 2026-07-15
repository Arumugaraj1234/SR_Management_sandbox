package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.IndentGrpScpVenEntity;

public class IndentGrpScpVenRowMapper implements RowMapper<IndentGrpScpVenEntity> {
	private static final Logger logger = LoggerFactory.getLogger(IndentGrpScpVenRowMapper.class);

	@Override
	public IndentGrpScpVenEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		IndentGrpScpVenEntity igs = new IndentGrpScpVenEntity();
		try {
			igs.setIgScpId(rs.getString("IG_SCS_ID"));
			igs.setIgScpVid(rs.getString("IG_SCS_VID"));
			igs.setL1VendorCode(rs.getString("L1_VENDOR_CODE"));
			igs.setL2VendorCode(rs.getString("L2_VENDOR_CODE"));
			igs.setL3VendorCode(rs.getString("L3_VENDOR_CODE"));
			igs.setL1VendorCountry(rs.getString("L1_VENDOR_COUNTRY"));
			igs.setL2VendorCountry(rs.getString("L2_VENDOR_COUNTRY"));
			igs.setL3VendorCountry(rs.getString("L3_VENDOR_COUNTRY"));
			igs.setL1VendorCurrency(rs.getString("L1_VENDOR_CURRENCY"));
			igs.setL2VendorCurrency(rs.getString("L2_VENDOR_CURRENCY"));
			igs.setL3VendorCurrency(rs.getString("L3_VENDOR_CURRENCY"));
			igs.setLevel(rs.getString("LEVEL"));
			igs.setLastUpdatedBy(rs.getString("LAST_UPDATED_BY"));
			igs.setLastUpdatedDate(rs.getString("LAST_UPDATED_DATETIME"));
			igs.setL1Gst(rs.getString("L1_GST"));
			igs.setL2Gst(rs.getString("L2_GST"));
			igs.setL3Gst(rs.getString("L3_GST"));
		} catch (Exception ex) {
			logger.error("IndentGrpScpVenRowMapper error " + ex);
		}
		return igs;
	}
} 
