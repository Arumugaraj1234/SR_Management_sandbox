package com.vmfg.mis.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProjSpentDrillDownEntity {

	private String projCode;
	private String projName;
	private String pmHdrId;
	private String budgetConsumed;
	private String materialBudCons;
	private String serviceBudCons;
	private String materialRelesVal;
	private String serviceRelesVal;
	private String actualVal;
}
