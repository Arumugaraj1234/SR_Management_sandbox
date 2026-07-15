package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesBudgetSheetRequest {

	private String masterId;
	private String tenantId;
	private String isBudget;
	
}
