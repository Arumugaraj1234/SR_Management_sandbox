package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRequestHdrInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String masterId;
	private String totalBudgetCost;
	private String finalSaleValue;
	private String budgetCost;
	
	private String crCost;
	private String remarks;
	private String crValue;
	private String crDateTime;
	private String sbHdrId;
	private String sbcId;

}
