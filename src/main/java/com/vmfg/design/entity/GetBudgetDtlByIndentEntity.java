package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBudgetDtlByIndentEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String	pkseId;
	private String	pkaId;
	private String sbExtnId;
	private String	allocatedQty;
	private String	allocatedValue;
	private String	balanceQty;
	private String	balanceValue;
	private String	elementHdr;
	private String	elementDtl;
	private String	unitPrice;
	private String	requiredQty;
	private String	requiredValue;
	private String elementSpec;
	private String elementMake;
}
