package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllTaskTypeRequest {

	private String tenantId;
	private String deptCode;
}
