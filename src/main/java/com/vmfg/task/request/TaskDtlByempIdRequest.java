package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDtlByempIdRequest {

	private String empId;
	private String tenantId ;
	private String pmId;
	private String depCode;
}
