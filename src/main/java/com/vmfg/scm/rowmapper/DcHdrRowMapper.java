package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.DcHdrEntity;

public class DcHdrRowMapper implements RowMapper<DcHdrEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetPoDtlsByDateRowMapper.class);

	@Override
	public DcHdrEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		DcHdrEntity deliveryChallan = new DcHdrEntity();
		try {

			if (columnExists(rs, "PO_CODE")) {
				deliveryChallan.setPoCode(rs.getString("PO_CODE"));
			}
			if (columnExists(rs, "INDENT_DTL_ID")) {
				deliveryChallan.setIndentDtlId(rs.getString("INDENT_DTL_ID"));
			}
			if (columnExists(rs, "PO_DTL_ID")) {
				deliveryChallan.setPoDtlId(rs.getString("PO_DTL_ID"));
			}
			
			if (columnExists(rs, "PROJECT_CODE")) {
				deliveryChallan.setProjectCode(rs.getString("PROJECT_CODE"));
			}
			if (columnExists(rs, "PROJECT_NAME")) {
				deliveryChallan.setProjectName(rs.getString("PROJECT_NAME"));
			}

			deliveryChallan.setDcID(rs.getString("DC_ID"));
			deliveryChallan.setDcCode(rs.getString("DC_CODE"));
			deliveryChallan.setDcType(rs.getString("DC_TYPE"));
			deliveryChallan.setDcDate(rs.getString("DC_DATE"));
			deliveryChallan.setPmHdrId(rs.getString("PM_HDR_ID"));
			deliveryChallan.setPoNO(rs.getString("PO_NO"));
			deliveryChallan.setPoDATE(rs.getString("PO_DATE"));
			deliveryChallan.setTransportationName(rs.getString("TRANSPORTATION_NAME"));
			deliveryChallan.setTransportationMode(rs.getString("TRANSPORTATION_MODE"));
			deliveryChallan.setLrDateTime(rs.getString("LR_DATE_TIME"));
			deliveryChallan.setLrNo(rs.getString("LR_NO"));
			deliveryChallan.setShippedFrom(rs.getString("SHIPPED_FROM"));
			deliveryChallan.setShippedFromAddress(rs.getString("SHIPPED_FROM_ADDRESS"));
			deliveryChallan.setShippedFromDistrict(rs.getString("SHIPPED_FROM_DISTRICT"));
			deliveryChallan.setShippedFromState(rs.getString("SHIPPED_FROM_STATE"));
			deliveryChallan.setShippedFromCountry(rs.getString("SHIPPED_FROM_COUNTRY"));
			deliveryChallan.setDeliveredTo(rs.getString("DELIVERED_TO"));
			deliveryChallan.setDeliveredToAddress(rs.getString("DELIVERED_TO_ADDRESS"));
			deliveryChallan.setDeliveredToDistrict(rs.getString("DELIVERED_TO_DISTRICT"));
			deliveryChallan.setDeliveredToState(rs.getString("DELIVERED_TO_STATE"));
			deliveryChallan.setDeliveredToCountry(rs.getString("DELIVERED_TO_COUNTRY"));
			deliveryChallan.setAmountInWords(rs.getString("AMOUNT_IN_WORDS"));
			deliveryChallan.setRemarks(rs.getString("REMARKS"));
			deliveryChallan.setTotalBasic(rs.getString("TOTAL_BASIC"));
			deliveryChallan.setGstValue(rs.getString("GST_VALUE"));
			deliveryChallan.setTotalValue(rs.getString("TOTAL_VALUE"));
			deliveryChallan.setTenantId(rs.getString("TENANT_ID"));
			// deliveryChallan.setDcTypeDesc(rs.getString("DC_TYPE_DESC"));
			deliveryChallan.setShippedGstIn(rs.getString("SHIPPED_FROM_GSTIN"));
			deliveryChallan.setShippedPinCode(rs.getString("SHIPPED_FROM_PINCODE"));
			deliveryChallan.setDeliveredPinCode(rs.getString("DELIVERED_FROM_PINCODE"));
			deliveryChallan.setDeliveredGstIn(rs.getString("DELIVERED_FROM_GSTIN"));
			deliveryChallan.setDivision(rs.getString("DIVISION"));
			deliveryChallan.setRecNo(rs.getString("REC_NO"));
			deliveryChallan.setIsCancel(rs.getString("IS_CANCEL"));
		} catch (Exception e) {
			logger.error("DcHdrRowMapper Exception--->" + e);
		}
		return deliveryChallan;
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
