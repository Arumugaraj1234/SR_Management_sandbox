package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskReassignRequest {

	private String taskdtlId;
	private String empId;
	private String tenantId;
	private String pmId;
}
