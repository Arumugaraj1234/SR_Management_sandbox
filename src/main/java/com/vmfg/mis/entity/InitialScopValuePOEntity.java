package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InitialScopValuePOEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String PoNumber;
	private String indentCode;
	private String deliveryLocation;
	private String poType;
	private String vendorName;
	private String totalValue;
	private String indentBasicTotal;
	private String indentFinalTotal;
	private String poDate;
	private String diffrence;
	private String projCode;
	private String station;
	private String subAssy;
	private String indentTypeDesc;
	
}
