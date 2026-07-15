package com.vmfg.design.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesIndentBudgetDtlEntity {

	private String indentBudId;
	private String indentId;
	private String pkaId;
	private String sbExtnId;
	private String requiredQty;
	private String requiredValue;
	private String oldRequiredQty;
	private String oldRequiredValue;
	private	String tenantId;
}
