package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskDtlPtgRequest {

	private String teDtlId;
	private String PtgVal;
	private String remarks;
	private String employeeId;
	private	String tenantId;
}
