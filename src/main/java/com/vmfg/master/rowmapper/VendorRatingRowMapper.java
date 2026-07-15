package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.VendorRatingEntity;

public class VendorRatingRowMapper implements RowMapper<VendorRatingEntity> {
	private static final Logger logger = LoggerFactory.getLogger(VendorRatingRowMapper.class);

	@Override
	public VendorRatingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		VendorRatingEntity res = new VendorRatingEntity();
		try {
			VendorRatingEntity vendorInspection = new VendorRatingEntity();
			vendorInspection.setVdtlId(rs.getString("VDTL_ID"));
			vendorInspection.setVendorCode(rs.getString("VENDOR_CODE"));
			vendorInspection.setInspectionDate(rs.getString("INSPECTION_DATE"));
			vendorInspection.setInspectionRating(rs.getString("INSPECTION_RATING"));
			vendorInspection.setInspectedOn(rs.getString("INSPECTED_ON"));
			vendorInspection.setInspectedBy(rs.getString("INSPECTED_BY"));
//			vendorInspection.setVendorName(rs.getString("VENDOR_NAME"));
			return vendorInspection;
		} catch (Exception ex) {
			logger.error("VendorRatingRowMapper  Method Exception" + ex);
		}
		return res;
	}
}