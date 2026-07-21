package com.vmfg.finance.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.finance.entity.GetPraDtlEntity;

public class GetPraDtlRowMapper implements RowMapper<GetPraDtlEntity> {
	private static final Logger logger = LoggerFactory.getLogger(GetPraDtlRowMapper.class);
	@Override
	public GetPraDtlEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
		GetPraDtlEntity row = new GetPraDtlEntity();
		try {
			row.setAmountPayable(rs.getString("AMOUNT_PAYABLE"));
			row.setRetention(rs.getString("RETENTION"));
			row.setTransportValue(rs.getString("TRANSPORT_CHARGE"));
			row.setPfValue(rs.getString("P_F_CHARGE"));
			row.setInsuranceValue(rs.getString("INSURANCE_CHARGE"));
			row.setOtherValue(rs.getString("OTHER_CHARGE"));
			row.setLd(rs.getString("LD"));
			row.setOthers(rs.getString("OTHERS"));
			row.setCompletedDateTime(rs.getString("COMPLETED_DATETIME"));
			row.setDeliveryType(rs.getString("DELIVERY_TYPE"));
			row.setDueDate(rs.getString("DUE_DATE"));
			row.setGrnHdrId(rs.getString("GRN_HDR_ID"));
			row.setInvoiceDate(rs.getString("INVOICE_DATE"));
			row.setInvoiceNumber(rs.getString("INVOICE_NO"));
			row.setInvoiceValue(rs.getString("INVOICE_VALUE"));
			row.setIsCompleted(rs.getString("IS_COMPLETED"));
			row.setLastUpdatedDateTime(rs.getString("LAST_UPDATED_DATETIME"));
			row.setLastUpdatedOn(rs.getString("LAST_UPDATED_ON"));
			row.setOrderValue(rs.getString("ORDER_VALUE"));
			row.setPaymentTerms(rs.getString("PAYMENT_TERMS"));
			row.setPmHdrId(rs.getString("PM_HDR_ID"));
			row.setPoCode(rs.getString("PO_CODE"));
			row.setRevision(rs.getString("REVISION"));
			row.setRevisionDate(rs.getString("REVISION_DATE"));
			row.setPraCode(rs.getString("PRA_CODE"));
			row.setPoDate(rs.getString("PO_DATE"));
			row.setPoId(rs.getString("PO_ID"));
			row.setPotId(rs.getString("POT_ID"));
			row.setPraDate(rs.getString("PRA_DATE"));
			row.setPraId(rs.getString("PRA_ID"));
			row.setRemarks(rs.getString("REMARKS"));
			row.setStatus(rs.getString("STATUS"));
			row.setStatusDesc(rs.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			row.setTds(rs.getString("TDS"));
			row.setTenantId(rs.getString("TENANT_ID"));
			row.setTypeOfPayment(rs.getString("TYPE_OF_PAYMENT"));
			row.setVendorCode(rs.getString("VENDOR_CODE"));
			row.setVendorName(rs.getString("VENDOR_NAME"));
			row.setProjectCode(rs.getString("PROJECT_CODE"));
			row.setProjectName(rs.getString("PROJECT_NAME"));
			row.setPoType(rs.getString("PO_TYPE"));
			row.setPoValue(rs.getString("TOTAL_VALUE"));
			row.setIsLatest(rs.getString("IS_LATEST"));
			row.setBasicTotal(rs.getString("BASIC_TOTAL"));
			row.setGst(rs.getString("GST"));
			if (columnExists(rs, "GST_VALUE")) {
				row.setPraGst(rs.getString("GST_VALUE"));
			}
			if (columnExists(rs, "PO_QTY")) {
				row.setPoQty(rs.getString("PO_QTY"));
			}
		} catch (Exception e) {
			logger.error("Exception in GetPraDtlRowMapper" + e);

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
