package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductBasedInventoryDtlEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String inventoryLocationCode;
	private String productQtyOnHand;
	private String inventoryLocationDesc;
	private String inventoryLocationType;
	private String inventoryLocationParentCode;





}
