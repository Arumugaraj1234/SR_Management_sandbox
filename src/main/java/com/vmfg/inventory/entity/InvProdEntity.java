package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvProdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String prodCode;
	private String prodDesc;
	private String uom;
	private String qtyOnHand;
	private String weight;
	private String projCode;
	private String locationDesc;
	private String customerName;
	private String projectId;
	private String productId;
	private String poCode;
	private String unitRate;
	private String bin;
	private String make;
	private String specification;
	private String invValue;
	
}
