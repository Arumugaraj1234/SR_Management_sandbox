package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SalesBudgetSheetDtlEntity  implements Serializable{

	
	private static final long serialVersionUID = 1L;

	
	private String sbDtlId;
	private String sbcDesc;
	private String value;
	private String sbcId;
	
}
