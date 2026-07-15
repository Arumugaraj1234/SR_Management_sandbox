package com.vmfg.inventory.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailableProductsForTransferRequest {

	private String tenantId;
	private String pmHdrId;
	private String locationCode;

}
