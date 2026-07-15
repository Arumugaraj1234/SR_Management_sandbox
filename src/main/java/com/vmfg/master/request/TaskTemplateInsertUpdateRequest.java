package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskTemplateInsertUpdateRequest {

	private String ttDtlId;
	private String ttHdrId;
	private String actName;
	private String isActive;
	private String empId;
	private String tenantId;
}
