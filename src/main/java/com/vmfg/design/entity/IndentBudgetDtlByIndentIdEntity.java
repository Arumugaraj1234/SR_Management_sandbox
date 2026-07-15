package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentBudgetDtlByIndentIdEntity implements Serializable {

	private static final long serialVersionUID = 1L;
 
	private String indentBudId;
	private String indentId;
	private String pkaId;
	private String sbExtnId;
	private String budgetQty;
	private String budgetValue;
	private String tenantId;
}
