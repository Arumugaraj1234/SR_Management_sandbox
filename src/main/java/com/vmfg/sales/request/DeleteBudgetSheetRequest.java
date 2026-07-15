package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteBudgetSheetRequest {

	private String sbcId;
	private String tenantId;
	private String hdrId;
	
}
