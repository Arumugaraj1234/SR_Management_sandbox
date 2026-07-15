package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetIndentDtlProductCostRequest {

	private String productCode;
	private String tenantId;
	private String pmHdrId;
	private String productId;
}
