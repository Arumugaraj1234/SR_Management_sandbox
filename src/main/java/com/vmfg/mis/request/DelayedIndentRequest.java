package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DelayedIndentRequest {
	private String tenantId;
	private String pmId;
	private String lifeSpan;
	private String monthYear;
	private String empId;
	private String projectId;
}
