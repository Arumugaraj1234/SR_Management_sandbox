package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoInstoreDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productCode;
	private String productDesc;
	private String productId;
	private String pmHdrId;
	private String uomCode;
	private String uomLongDescriprtion;
	private String uomShortDescriprtion;
	private String pkaId;
	private String pkaDesc;
	private String pskaId;
	private String pskaDesc;
	private String make;
	private String qty;
	private String unit;
	private String weight;
	private String material;
	private String lastupdatedUserId;
	private String lastupdatedDateTime;
	private String productQtyOnHand;
	
	
}
