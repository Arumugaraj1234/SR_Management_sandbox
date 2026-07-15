package com.vmfg.inventory.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailableQtyRequest { 
	
	private String productID;
	private String frmLocationCode;
	private String tenantID;

}
