package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskCategoryByPmHdrIdRequest {

	private String taskTypeCode;
	private String tenantId;
	private String pmHdrId;
}
