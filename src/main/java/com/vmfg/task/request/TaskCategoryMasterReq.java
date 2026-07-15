package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCategoryMasterReq {
	private String taskCategoryCode;
	private String taskCategoryDesc;
	private String taskTypeCode;
	private String tenantID;
	private String isActive; 
}
