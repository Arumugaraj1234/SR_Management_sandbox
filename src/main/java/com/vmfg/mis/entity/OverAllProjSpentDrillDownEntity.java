package com.vmfg.mis.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverAllProjSpentDrillDownEntity {
 
	private String projCode;
	private String projName;
	private String pmHdrId;
	private String custName;
	private String stage;
	private String orderValue;
	private String projcBudget;
	private String actualVal;
	private String materialBudCons;
	private String serviceBudCons;
	private String materialRelesVal;
	private String serviceRelesVal;
	private String totalPoreles;
	private String totalBudgetConsum;
	private String scmAllocatedVal;
	private String empCost;
	private String materialTransferCost;
	private String cashVochar;
	private String otherInTally;
	private String debitValue;
}
