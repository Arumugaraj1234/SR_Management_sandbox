package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.GetDebitNoteEntity;

public class GetDebitNoteHdrRowMapper implements RowMapper<GetDebitNoteEntity>  {
	private static final Logger logger = LoggerFactory.getLogger(GetDebitNoteHdrRowMapper.class);
	@Override
	public GetDebitNoteEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetDebitNoteEntity row = new GetDebitNoteEntity();
		try {
			if (columnExists(rs, "DN_ID")) {
				row.setDnId(rs.getString("DN_ID"));
			}
			if (columnExists(rs, "PROJECT_CODE")) {
				row.setProjectCode(rs.getString("PROJECT_CODE"));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
				row.setProjectName(rs.getString("PROJECT_NAME"));
			}
			if (columnExists(rs, "DN_VALUE")) {
				row.setDnValue(rs.getString("DN_VALUE"));
			}
			if (columnExists(rs, "PO_CODE")) {
				row.setPoCode(rs.getString("PO_CODE"));
			}
			if (columnExists(rs, "PO_TYPE")) {
				row.setPoType(rs.getString("PO_TYPE"));
			}
			if (columnExists(rs, "PO_ID")) {
				row.setPoId(rs.getString("PO_ID"));
			}
			if (columnExists(rs, "VENDOR_NAME")) {
				row.setVendorName(rs.getString("VENDOR_NAME"));
			}
			if (columnExists(rs, "VENDOR_CODE")) {
				row.setVendorCode(rs.getString("VENDOR_CODE"));
			}
			if (columnExists(rs, "DNR_REASON")) {
				row.setReason(rs.getString("DNR_REASON"));
			}
			if (columnExists(rs, "CREATED_BY")) {
				row.setCreatedBy(rs.getString("CREATED_BY"));
			}
			if (columnExists(rs, "TENANT_ID")) {
				row.setTenantId(rs.getString("TENANT_ID"));
			}
			if (columnExists(rs, "TOTAL_VALUE")) {
				row.setPoValue(rs.getString("TOTAL_VALUE"));
			}
			if (columnExists(rs, "TOTAL_VALUE_FX")) {
				row.setPoValueFx(rs.getString("TOTAL_VALUE_FX"));
			}
			if (columnExists(rs, "SEQUENCE_NO")) {
				row.setSeqno(rs.getString("SEQUENCE_NO"));
			}
			if (columnExists(rs, "SEQUENCE_STATUS")) {
				row.setSeqStatus(rs.getString("SEQUENCE_STATUS"));
			}
			if (columnExists(rs, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				row.setSeqDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			}
			row.setBillingName(rs.getString("BILLING_NAME") != null ? rs.getString("BILLING_NAME") : "");
			row.setBillingAddressLine(rs.getString("BILLING_ADDRESS_LINE") != null ? rs.getString("BILLING_ADDRESS_LINE") : "");
			row.setBillingCity(rs.getString("BILLING_CITY") != null ? rs.getString("BILLING_CITY") : "");
			row.setBillingPincode(rs.getString("BILLING_PINCODE") != null ? rs.getString("BILLING_PINCODE") : "");
			row.setBillingState(rs.getString("BILLING_STATE") != null ? rs.getString("BILLING_STATE") : "");
			row.setBillingCount(rs.getString("BILLING_COUNT") != null ? rs.getString("BILLING_COUNT") : "");
			row.setBillingGst(rs.getString("BILLING_GST") != null ? rs.getString("BILLING_GST") : "");
			row.setVendorAddressLine(rs.getString("VENDOR_ADDRESS_LINE") != null ? rs.getString("VENDOR_ADDRESS_LINE") : "");
			row.setVendorCity(rs.getString("VENDOR_CITY") != null ? rs.getString("VENDOR_CITY") : "");
			row.setVendorPincode(rs.getString("VENDOR_PINCODE") != null ? rs.getString("VENDOR_PINCODE") : "");
			row.setVendorGst(rs.getString("VENDOR_GST") != null ? rs.getString("VENDOR_GST") : "");
			row.setDeliveryName(rs.getString("DELIVERY_NAME") != null ? rs.getString("DELIVERY_NAME") : "");
			row.setDeliveryAddressLine(rs.getString("DELIVERY_ADDRESS_LINE") != null ? rs.getString("DELIVERY_ADDRESS_LINE") : "");
			row.setDeliveryCity(rs.getString("DELIVERY_CITY") != null ? rs.getString("DELIVERY_CITY") : "");
			row.setDeliveryPincode(rs.getString("DELIVERY_PINCODE") != null ? rs.getString("DELIVERY_PINCODE") : "");
			row.setDeliveryState(rs.getString("DELIVERY_STATE") != null ? rs.getString("DELIVERY_STATE") : "");
			row.setDeliveryCount(rs.getString("DELIVERY_COUNT") != null ? rs.getString("DELIVERY_COUNT") : "");
			row.setDeliveryGst(rs.getString("DELIVERY_GST") != null ? rs.getString("DELIVERY_GST") : "");
			row.setDeliveryContact(rs.getString("DELIVERY_CONTACT") != null ? rs.getString("DELIVERY_CONTACT") : "");


		} catch (Exception e) {
			logger.error("Exception in GetDebitNoteHdrRowMapper" + e);

		}

		return row;
	}
	
	private boolean columnExists(ResultSet rs, String columnName) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columns = metaData.getColumnCount();

		for (int i = 1; i <= columns; i++) {
			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
				return true;
			}
		}

		return false;
	}

}
