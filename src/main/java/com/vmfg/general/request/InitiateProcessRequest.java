package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiateProcessRequest {

	private String deptCode;
	private String tenantId;
	private String refId;
	private String dueDate;
	private String empID;
	private String startDate;
	private String pmId;
}
