package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDtlByDcTypeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String address;
	private String city;
	private String pincode;
	private String state;
	private String GstNo;
	private String code;
	private String contactNo;
	private String locationId;
}
