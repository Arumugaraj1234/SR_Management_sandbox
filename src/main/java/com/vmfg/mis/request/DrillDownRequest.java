package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DrillDownRequest {
	private String projectId;
	private String tenantId;
	private String empId;
	private String pmId;
	private String lifeSpan;
	private String monthYear;
}
