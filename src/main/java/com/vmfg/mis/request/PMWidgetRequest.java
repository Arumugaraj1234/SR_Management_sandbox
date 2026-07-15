package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PMWidgetRequest {
	private String fromDate;
	private String endDate;
	private String tenantId;
	private String pmHdrId;
	private String sbcCode;
}
