package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryAdjustmentEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String sno;
	private String projectCode;
	private String projectDesc;
	private String productCode;
	private String productDesc;
	private String uom;
	private String locationCode;
	private String locationDesc;
	private String adjustmentType;
	private String adjustedBy;
	private String adjustedDateTime;
	private String qtyonHand;
	private String adjustmentQty;
	private String revisedqtyonHand;
	private String reason;
	private String adjustmentCode;
	


}
