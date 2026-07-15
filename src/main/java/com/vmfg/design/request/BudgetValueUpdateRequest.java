package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetValueUpdateRequest {
	private String budgetValue;
	private String indentId;
	private String tenantId;
	private String date;
	private String targetValue;

}
