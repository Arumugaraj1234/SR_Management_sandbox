package com.vmfg.design.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatbaseIndentCostDtlEntity {

	private String totalBudgetCost;
	private String totalPoCost;
	private String finalBudgetStatus;
	private String totalTargetCost;
	private List<IndentCostDtlEntity> indentCostDtl;

	private String consumedSoFar;
	private String budgetExcessApproved;
}
