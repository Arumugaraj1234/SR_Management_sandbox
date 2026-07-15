package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskPercentFlagRequest {

	private String dtlId;
	private String tenantId;
	private String employeeId;
}
