package com.vmfg.mis.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorDetailDrillDownEntity {

	private String projCode;
	private String projName;
	private String pmHdrId;
	private String vendorName;
	private String poCode;
	private String poDate;
	private String praCode;
	private String praDate;
	private String amountPayable;
	private String amountDue;
	private String paidSoFar;
	private String dueDate;
	private String overDue;
	private String status;
	private String invoiceNo;
	private String invoiceDate;
}
