package com.vmfg.mis.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjConsumedValEntity {

	private String budgetConsumed;
	private String projBudget;
//	private String budgetForService;
	private String poReleased;
	private String actualSpend;
//	private String poForMaterial;
//	private String poForService;
	private String balanceAvailable;
}
