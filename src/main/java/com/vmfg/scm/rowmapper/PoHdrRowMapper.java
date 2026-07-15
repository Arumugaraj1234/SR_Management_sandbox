package com.vmfg.scm.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.vmfg.scm.entity.GetPoDtlsEntity;

public class PoHdrRowMapper implements RowMapper<GetPoDtlsEntity> {
    private static final Logger logger = LoggerFactory.getLogger(PoHdrRowMapper.class);

    @Override
    public GetPoDtlsEntity mapRow(ResultSet row, int rowNum) throws SQLException {
    	GetPoDtlsEntity po = new GetPoDtlsEntity();
        try {
        	po.setPoId(row.getString("PO_ID") != null ? row.getString("PO_ID") : "");
            po.setIgScpId(row.getString("IG_SCS_ID") != null ? row.getString("IG_SCS_ID") : "");
            if (columnExists(row, "IG_HDR_ID")) {
                po.setIgHdrId(row.getString("IG_HDR_ID"));
            }
            po.setTransactionNo(row.getString("TRANSACTION_NO") != null ? row.getString("TRANSACTION_NO") : "");
            po.setFinancialYearMstId(row.getString("FINANCIAL_YEAR_MST_ID") != null ? row.getString("FINANCIAL_YEAR_MST_ID") : "");
            po.setPoType(row.getString("PO_TYPE") != null ? row.getString("PO_TYPE") : "");
            po.setBillingName(row.getString("BILLING_NAME") != null ? row.getString("BILLING_NAME") : "");
            po.setBillingAddressLine(row.getString("BILLING_ADDRESS_LINE") != null ? row.getString("BILLING_ADDRESS_LINE") : "");
            po.setBillingCity(row.getString("BILLING_CITY") != null ? row.getString("BILLING_CITY") : "");
            po.setBillingPincode(row.getString("BILLING_PINCODE") != null ? row.getString("BILLING_PINCODE") : "");
            po.setBillingState(row.getString("BILLING_STATE") != null ? row.getString("BILLING_STATE") : "");
            po.setBillingCount(row.getString("BILLING_COUNT") != null ? row.getString("BILLING_COUNT") : "");
            po.setBillingGst(row.getString("BILLING_GST") != null ? row.getString("BILLING_GST") : "");
            po.setVendorName(row.getString("VENDOR_NAME") != null ? row.getString("VENDOR_NAME") : "");
            po.setVendorAddressLine(row.getString("VENDOR_ADDRESS_LINE") != null ? row.getString("VENDOR_ADDRESS_LINE") : "");
            po.setVendorCity(row.getString("VENDOR_CITY") != null ? row.getString("VENDOR_CITY") : "");
            po.setVendorPincode(row.getString("VENDOR_PINCODE") != null ? row.getString("VENDOR_PINCODE") : "");
            po.setVendorState(row.getString("VENDOR_STATE") != null ? row.getString("VENDOR_STATE") : "");
            po.setVendorCount(row.getString("VENDOR_COUNT") != null ? row.getString("VENDOR_COUNT") : "");
            po.setVendorGst(row.getString("VENDOR_GST") != null ? row.getString("VENDOR_GST") : "");
            po.setDeliveryName(row.getString("DELIVERY_NAME") != null ? row.getString("DELIVERY_NAME") : "");
            po.setDeliveryAddressLine(row.getString("DELIVERY_ADDRESS_LINE") != null ? row.getString("DELIVERY_ADDRESS_LINE") : "");
            po.setDeliveryCity(row.getString("DELIVERY_CITY") != null ? row.getString("DELIVERY_CITY") : "");
            po.setDeliveryPincode(row.getString("DELIVERY_PINCODE") != null ? row.getString("DELIVERY_PINCODE") : "");
            po.setDeliveryState(row.getString("DELIVERY_STATE") != null ? row.getString("DELIVERY_STATE") : "");
            po.setDeliveryCount(row.getString("DELIVERY_COUNT") != null ? row.getString("DELIVERY_COUNT") : "");
            po.setDeliveryGst(row.getString("DELIVERY_GST") != null ? row.getString("DELIVERY_GST") : "");
            po.setDeliveryContact(row.getString("DELIVERY_CONTACT") != null ? row.getString("DELIVERY_CONTACT") : "");
            po.setDivision(row.getString("DIVISION") != null ? row.getString("DIVISION") : "");
            po.setOrderNo(row.getString("ORDER_NO") != null ? row.getString("ORDER_NO") : "");
            po.setDate(row.getString("DATE") != null ? row.getString("DATE") : "");
            po.setRevision(row.getString("REVISION") != null ? row.getString("REVISION") : "");
            po.setRevisionDate(row.getString("REVISION_DATE") != null ? row.getString("REVISION_DATE") : "");
            po.setRefDate(row.getString("REF_DATE") != null ? row.getString("REF_DATE") : "");
            po.setDeliveryTerms(row.getString("DELIVERY_TERMS") != null ? row.getString("DELIVERY_TERMS") : "");
            po.setLiqDamages(row.getString("LIQ_DAMAGES") != null ? row.getString("LIQ_DAMAGES") : "");
            po.setGuarantee(row.getString("GUARANTEE") != null ? row.getString("GUARANTEE") : "");
            po.setWarrenty(row.getString("WARRENTY") != null ? row.getString("WARRENTY") : "");
            po.setDispatchMode(row.getString("DISPATCH_MODE") != null ? row.getString("DISPATCH_MODE") : "");
            po.setTransitInsurance(row.getString("TRANSIT_INSURANCE") != null ? row.getString("TRANSIT_INSURANCE") : "");
            po.setInspectionScope(row.getString("INSPECTION_SCOPE") != null ? row.getString("INSPECTION_SCOPE") : "");
            po.setMisc(row.getString("MISC") != null ? row.getString("MISC") : "");
            po.setPortOfDest(row.getString("PORT_OF_DEST") != null ? row.getString("PORT_OF_DEST") : "");
            po.setRemarks(row.getString("REMARKS") != null ? row.getString("REMARKS") : "");
            po.setDiscount(row.getString("DISCOUNT") != null ? row.getString("DISCOUNT") : "");
            po.setPF(row.getString("P_F") != null ? row.getString("P_F") : "");
            po.setPFFX(row.getString("P_F_FX") != null ? row.getString("P_F_FX") : "");
            po.setFrieght(row.getString("FRIEGHT") != null ? row.getString("FRIEGHT") : "");
            po.setOthers(row.getString("OTHERS") != null ? row.getString("OTHERS") : "");
            po.setBasicTotal(row.getString("BASIC_TOTAL") != null ? row.getString("BASIC_TOTAL") : "");
            po.setBasicTotalFx(row.getString("BASIC_TOTAL_FX") != null ? row.getString("BASIC_TOTAL_FX") : "");
            po.setGst(row.getString("GST") != null ? row.getString("GST") : "");
            po.setGstFx(row.getString("GST_FX") != null ? row.getString("GST_FX") : "");
            po.setCess(row.getString("CESS") != null ? row.getString("CESS") : "");
            po.setTotalValue(row.getString("TOTAL_VALUE") != null ? row.getString("TOTAL_VALUE") : "");
            po.setTotalValueFx(row.getString("TOTAL_VALUE_FX") != null ? row.getString("TOTAL_VALUE_FX") : "");
            po.setFrieghtRemarks(row.getString("FRIEGHT_REMARKS") != null ? row.getString("FRIEGHT_REMARKS") : "");
            po.setPFRemarks(row.getString("P_F_REMARKS") != null ? row.getString("P_F_REMARKS") : "");
            po.setPoTC(row.getString("PO_T_C") != null ? row.getString("PO_T_C") : "");
            po.setDwgs(row.getString("DWGS") != null ? row.getString("DWGS") : "");
            po.setQap(row.getString("QAP") != null ? row.getString("QAP") : "");
            po.setGtc(row.getString("GTC") != null ? row.getString("GTC") : "");
            po.setDocCharges(row.getString("DOC_CHARGES") != null ? row.getString("DOC_CHARGES") : "");
            po.setInspectionCharges(row.getString("INSPECTION_CHARGES") != null ? row.getString("INSPECTION_CHARGES") : "");
            po.setInsuranceValue(row.getString("INSURANCE_VALUE") != null ? row.getString("INSURANCE_VALUE") : "");
            po.setTestingCharges(row.getString("TESTING_CHARGES") != null ? row.getString("TESTING_CHARGES") : "");
            po.setCtc(row.getString("CTC") != null ? row.getString("CTC") : "");
            po.setTdc(row.getString("TDC") != null ? row.getString("TDC") : "");
            po.setTds(row.getString("TDS") != null ? row.getString("TDS") : "");
            po.setTenantId(row.getString("TENANT_ID") != null ? row.getString("TENANT_ID") : "");
            po.setSequenceNo(row.getString("SEQUENCE_NO") != null ? row.getString("SEQUENCE_NO") : "");
            po.setSequenceStatus(row.getString("SEQUENCE_STATUS") != null ? row.getString("SEQUENCE_STATUS") : "");
            po.setPoCode(row.getString("PO_CODE") != null ? row.getString("PO_CODE") : "");
            po.setVendorCode(row.getString("VENDOR_CODE") != null ? row.getString("VENDOR_CODE") : "");
            po.setIsApproved(row.getString("IS_APPROVED") != null ? row.getString("IS_APPROVED") : "");
            po.setUpdatedBy(row.getString("LAST_UPDATED_BY") != null ? row.getString("LAST_UPDATED_BY") : "");
            po.setUpdatedOn(row.getString("LAST_UPDATED_DATETIME") != null ? row.getString("LAST_UPDATED_DATETIME") : "");
            po.setIndentID(row.getString("INDENT_ID") != null ? row.getString("INDENT_ID") : "");
            po.setDeliveryDate(row.getString("DELIVERY_DATE")!=null ? row.getString("DELIVERY_DATE") : "");
            po.setBillingContactNo(row.getString("BILLING_CONTACT")!=null ? row.getString("BILLING_CONTACT") : "");
            po.setVendorContactNo(row.getString("VENDOR_CONTACT")!=null ? row.getString("VENDOR_CONTACT") : "");
            po.setAmountinwords(row.getString("AMOUNT_IN_WORDS")!=null ? row.getString("AMOUNT_IN_WORDS") : "");
            po.setIgst(row.getString("IGST") != null ? row.getString("IGST") : "");
            po.setTerminalTax(row.getString("TERMINAL_TAX") != null ? row.getString("TERMINAL_TAX") : "");
            if (columnExists(row, "DOCUMENT_STATUS_TYPE_DESCRIPTION")) {
				po.setSeqStatusDesc(row.getString("DOCUMENT_STATUS_TYPE_DESCRIPTION"));
			}
            
            if (columnExists(row, "RAN_DIV_COMMTE")) {
                po.setRanDivCommte(row.getString("RAN_DIV_COMMTE")!=null ? row.getString("RAN_DIV_COMMTE") : "");
            }
            if (columnExists(row, "PAN")) {
                po.setPan(row.getString("PAN")!=null ? row.getString("PAN") : "");
            }
            if (columnExists(row, "PRICE_BASIS")) {
                po.setPriceBasis(row.getString("PRICE_BASIS")!=null ? row.getString("PRICE_BASIS") : "");
            }
            if (columnExists(row, "INDENT_CODE")) {
                po.setIndentCode(row.getString("INDENT_CODE")!=null ? row.getString("INDENT_CODE") : "");
            }
            po.setRefNo(row.getString("REF_NO"));
            po.setTransportCharges(row.getString("TRANSPORT_CHARGES"));
            po.setTransportChargesFx(row.getString("TRANSPORT_CHARGES_FX"));
            po.setVenCode(row.getString("VEN_CODE"));
        } catch (Exception e) {
            logger.error("PoHdrRowMapper Exception--->" + e);
        }
        return po;
    }
    
 // column checking purpose (column is there or not)
 	private boolean columnExists(ResultSet row, String columnName) throws SQLException {
 		ResultSetMetaData metaData = row.getMetaData();
 		int columns = metaData.getColumnCount();

 		for (int i = 1; i <= columns; i++) {
 			if (columnName.equalsIgnoreCase(metaData.getColumnLabel(i))) {
 				return true;
 			}
 		}

 		return false;
 	}
}