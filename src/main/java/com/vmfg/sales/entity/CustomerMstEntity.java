package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String custCode;
	private String custName;
	private String gst;
	private String pan;
	private String address;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private String contactNo;
	private String tenantId;
}
