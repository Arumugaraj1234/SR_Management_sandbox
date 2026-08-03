package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentProjectEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String projectCode;
	private String projectName;
	private String indentCode;
	private String createdDate;
	private String expDeliveryDate;
	private	String totalBudgetConsumed;
	private String costFlowType;
	private String allocatedValue;
	private String actualConsumedValue;
	private String canAllocateFromSalesBudget;
	private String isShortfall;
	private String hasBudgetExcess;
	private String pkaId;

}
