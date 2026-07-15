package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RetrieveForMSEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String inventoryQtyOnHand;
	private String productCode;
	private String station;
	private String subAssy;
	private String uomLongDesc;
	private String productDesc;
	private String productId;
	private String specification;
	private String make;
}
