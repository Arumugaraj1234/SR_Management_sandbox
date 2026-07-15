package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesBudgetSheetDtlRequest {
	private String sbDtlId;
	private String keyCategory;
	private String value;
	private String sbHdrId;
	private String tenantId;
}
