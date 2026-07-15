package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatesalesBudgetHdrRequest {

	private String sbHdrId;
	private String paymentTerms;
	private String salesPercent;
	private String finalSaleVal;
	private String tenantId;
}
