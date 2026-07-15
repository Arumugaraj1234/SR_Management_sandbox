package com.vmfg.inventory.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertAdjustmentRequest {
	
	private String projectId;
	private String productId;
	private String productCode;
	private String locationCode;
	private String adjustmentType;
	private String qtyonHand;
	private String adjustedQty;
	private String revisedQty;
	private String adjustmentedBy;
	private String reason;
	private String tenantId;
	

}
