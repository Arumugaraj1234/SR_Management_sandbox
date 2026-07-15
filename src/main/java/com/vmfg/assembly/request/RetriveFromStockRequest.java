package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetriveFromStockRequest {
	private String pmHdrId;
	private String pskaId;
	private String pkaId;
	private String tenantId;
}
