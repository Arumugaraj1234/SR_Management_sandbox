package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskRecordedRequest {

	private String tenantId;
	private String typeCode;
	private String subTask;
	private String deptCode;
}
