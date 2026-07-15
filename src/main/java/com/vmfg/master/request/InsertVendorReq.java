package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertVendorReq {

	private String vendorName; 
	private String gst;
	private String pan; 
	private String arn; 
	private String emailId; 
	private String vendorStatus; 
	private String poType;
	private String tenantId;
	private String locationReferenceName;
	private String locationAddressLine;
	private String locationCity;
	private String locationState; 
	private String locationConutryCode; 
	private String locationPinCode;
	private String vendorCode;
	private String locationId;
	private String contactNo;
	private String vendorType;
	private String supplyCategory;
	private String gstType;
	private String currencyType;
}
