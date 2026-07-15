package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApprovedBtnRequest {
	private String empID;
	private String tenantId;
	private String dmId;
	private String docTypeCode;
	private String pmId;

}
