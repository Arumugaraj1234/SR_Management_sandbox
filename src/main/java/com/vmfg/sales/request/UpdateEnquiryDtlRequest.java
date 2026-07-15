package com.vmfg.sales.request;

import java.util.List;

import com.vmfg.sales.entity.SalesEnqContactEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEnquiryDtlRequest {
	
	private String seId;
	private String enquiryNo;
	private String projectName;
	private String industrialType;
	private String scopeOfWork;
	private String projectDescription;
	private String productDetails;
	private String enquiryType;
	private String enquiryCustomerSts;
	private String reason;
	private String enquiryDate;
	private String leadDtl;
	private String tentativePoValue;
	private String tenantId;
	private String pmId;
	private String customerName;
	private String customerCode;
	private String location;
	private String emplId;
	private String expectedPoDate;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String address;
	private String contactNo;
	private String isInternal;
	private String pan;
	private String gst;
	private List<SalesEnqContactEntity> salesEntity;
	
}
