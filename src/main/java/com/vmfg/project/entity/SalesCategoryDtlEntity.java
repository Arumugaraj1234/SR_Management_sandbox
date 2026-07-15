package com.vmfg.project.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesCategoryDtlEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String budgetValue;
	private String scmBudgetAllocated;
	private String excessBudgetValue;
	private String categoryDesc;
	private String sbcCode;
	List<GetIndentBudgetDtlsEntity> list;
}
