package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VendorMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String vendorCode;
	private String vendorName;
	private String vendorUniqueCode;
	private String branchCode;
	private String gst;
	private String pan;
	private String arn;
	private String emailId;
	private String vendorStatus;
	private String potype;
	private String locationId;
    private String locationRefName;
    private String locAddressLine;
    private String locCity;
    private String locState;
    private String locCountryCode;
    private String locPinCode;
    private String tenantId;
    private String contactNo;
    private int vendorType;
    private String vendorCategory;
    private String gstType;
    private String currencyType;
    private String reInspectionDate;
    private String inspectionRaised;
    private String latestInspectionRating;
    private String latestInspectedDate;
    private String firstInspectionDate;
}
