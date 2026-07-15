package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentBudgetDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String indentBudId;
	private String indentDtlId;
//	private String pskId;
	private String pkseId;
	private String allocatedQty;
	private	String allocatedVal;
	private String expectedDeliveryDate;
}
