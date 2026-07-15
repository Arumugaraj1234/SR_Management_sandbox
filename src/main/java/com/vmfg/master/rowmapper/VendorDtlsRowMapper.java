package com.vmfg.master.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.master.entity.VendorMstEntity;

public class VendorDtlsRowMapper implements RowMapper<VendorMstEntity> {
	private static final Logger logger = LoggerFactory.getLogger(VendorDtlsRowMapper.class);

	@Override
	public VendorMstEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		VendorMstEntity res = new VendorMstEntity();
		try {
			if (columnExists(rs, "ARN")) {
			    res.setArn(rs.getString("ARN"));
			}
			if (columnExists(rs, "BRANCH_CODE")) {
			    res.setBranchCode(rs.getString("BRANCH_CODE"));
			}
			if (columnExists(rs, "EMAIL_ID")) {
			    res.setEmailId(rs.getString("EMAIL_ID"));
			}
			if (columnExists(rs, "GST")) {
			    res.setGst(rs.getString("GST"));
			}
			if (columnExists(rs, "LOCATION_ID")) {
			    res.setLocationId(rs.getString("LOCATION_ID"));
			}
			if (columnExists(rs, "PAN")) {
			    res.setPan(rs.getString("PAN"));
			}
			if (columnExists(rs, "TENANT_ID")) {
			    res.setTenantId(rs.getString("TENANT_ID"));
			}
			if (columnExists(rs, "VENDOR_CODE")) {
			    res.setVendorCode(rs.getString("VENDOR_CODE"));
			}
			if (columnExists(rs, "VENDOR_NAME")) {
			    res.setVendorName(rs.getString("VENDOR_NAME"));
			}
			if (columnExists(rs, "VENDOR_STATUS")) {
			    res.setVendorStatus(rs.getString("VENDOR_STATUS"));
			}
			if (columnExists(rs, "PO_TYPE")) {
			    res.setPotype(rs.getString("PO_TYPE"));
			}
			if (columnExists(rs, "LOCATION_ADDRESSLINE")) {
			    res.setLocAddressLine(rs.getString("LOCATION_ADDRESSLINE"));
			}
			if (columnExists(rs, "LOCATION_CITY")) {
			    res.setLocCity(rs.getString("LOCATION_CITY"));
			}
			if (columnExists(rs, "LOCATION_STATE")) {
			    res.setLocState(rs.getString("LOCATION_STATE"));
			}
			if (columnExists(rs, "LOCATION_COUNTRY_CODE")) {
			    res.setLocCountryCode(rs.getString("LOCATION_COUNTRY_CODE"));
			}
			if (columnExists(rs, "LOCATION_PINCODE")) {
			    res.setLocPinCode(rs.getString("LOCATION_PINCODE"));
			}
			if (columnExists(rs, "CONTACT_NO")) {
			    res.setContactNo(rs.getString("CONTACT_NO"));
			}
			if (columnExists(rs, "LOCATION_REFERENCENAME")) {
			    res.setLocationRefName(rs.getString("LOCATION_REFERENCENAME"));
			}
			if (columnExists(rs, "VENDOR_TYPE")) {
			    res.setVendorType(rs.getInt("VENDOR_TYPE"));
			}
			if (columnExists(rs, "REINSPECTION_DATE")) {
			    res.setReInspectionDate(rs.getString("REINSPECTION_DATE"));
			}
			if (columnExists(rs, "INSPECTION_RAISED")) {
			    res.setInspectionRaised(rs.getString("INSPECTION_RAISED"));
			}
			if (columnExists(rs, "LATEST_INSPECTION_RATING")) {
			    res.setLatestInspectionRating(rs.getString("LATEST_INSPECTION_RATING"));
			}
			if (columnExists(rs, "LATEST_INSPECTED_DATE")) {
			    res.setLatestInspectedDate(rs.getString("LATEST_INSPECTED_DATE"));
			}
			if (columnExists(rs, "FIRST_INSPECTION_DATE")) {
			    res.setFirstInspectionDate(rs.getString("FIRST_INSPECTION_DATE"));
			}
			if (columnExists(rs, "VENDOR_CATEGORY")) {
			    res.setVendorCategory(rs.getString("VENDOR_CATEGORY"));
			}
			if (columnExists(rs, "GST_TYPE")) {
			    res.setGstType(rs.getString("GST_TYPE"));
			}
			if (columnExists(rs, "CURRENCY_TYPE")) {
				res.setCurrencyType(rs.getString("CURRENCY_TYPE"));
			}
			if (columnExists(rs, "VENDOR_UNIQUE_CODE")) {
			    res.setVendorUniqueCode(rs.getString("VENDOR_UNIQUE_CODE"));
			}
			
		} catch (Exception ex) {
			logger.error("VendorDtlsRowMapper  Method Exception" + ex);
		}
		return res;
	}
	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
				return true;
			}
		}

		return false;
	}
}
