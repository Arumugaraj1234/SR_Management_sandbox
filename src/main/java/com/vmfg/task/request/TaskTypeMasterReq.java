package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskTypeMasterReq {
	private String taskTypeCode;
	private String taskDesc;
	private String deptCode;
	private String tenantID;
	private String isActive; 
}
