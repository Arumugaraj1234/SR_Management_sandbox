package com.vmfg.finance.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PraInsertRequest {
	private String praId;
	private String praCode;
	private String poId;
	private String poDate;
	private String potId;
	private String paymentTerms;
	private String praDate;
	private String pmHdrId;
	private String dueDate;
	private String typeOfPayment;
	private String deliveryType;
	private String vendorCode;
	private String grnHdrId;
	private String invoiceNumber;
	private String invoiceDate;
	private String orderValue;
	private String invoiceValue;
	private String tds;
	private String amountPayable;
	private String retention;
	private String ld;
	private String others;
	private String status;
	private String isCompleted;
	private String completedDateTime;
	private String lastUpdatedDateTime;
	private String lastUpdatedOn;
	private String remarks;
	private String tenantId;
	private String pmId;
	private String statusCode;
	private String enquiryId;
	private String processCode;
	private String poCode;
	private String empId;
	private String percentage;
	private String poCostType;
	private String transportValue;
	private String insuranceValue;
	private String pfValue;
	private String otherValue;
	private String isLast;
	private String gst;
	private String igst;
	List<PraInsertDtlRequest> dtl =new ArrayList<PraInsertDtlRequest>();
	

}
