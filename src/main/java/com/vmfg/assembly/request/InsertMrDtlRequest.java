package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMrDtlRequest {
	private String poductId;
	private String requestedQty;
	private String availableQty;
	private String inventoryLocation;
	private String tenantId;
	private String descOfGoods;
}
