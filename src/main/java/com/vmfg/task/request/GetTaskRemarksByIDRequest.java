package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskRemarksByIDRequest {

	private String tenantId;
	private String taskDtlId;
}
