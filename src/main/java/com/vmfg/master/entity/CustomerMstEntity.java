package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String customerCode;
	private String customerName;
	private String gstNumber;
	private String panNumber;
	private String address;
	private String city;
	private String state;
	private String contactNumber;
	private String country;
	private String pincode;
}
