package com.vmfg.inventory.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class getQtyAvailLocRequest {
	private String productId;
	private String tenantID;
}
