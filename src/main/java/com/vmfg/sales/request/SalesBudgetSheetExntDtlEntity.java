package com.vmfg.sales.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesBudgetSheetExntDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sbExtnId;
	private String sbDtld;
	private String specification;
	private String make;
	private String value;
	private String Qty;
	private String totalValue;
	private String allocatedQty;
	private String allocatedvalue;
	private String elementHdr;
	private String elementDtl;
	private String tenantId;
	private String Subtotal;
	private String contingency;
	private String critical;
	private String timelineInWeeks;
	
	
}
