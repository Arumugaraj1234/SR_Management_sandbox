package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingDetailEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String orgCode;
	private String orgName;
	private String locationId;
	private String locationRefName;
	private String locAddressLine;
	private String locCity;
	private String locState;
	private String locCountryCode;
	private String locPinCode;
	private String contactNo;
	private String gstNo;

}