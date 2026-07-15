package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskTemplateTypeRequest {

	private String deptCode;
	private String ttCode;
	private String tcCode;
	private String tenantId;
}
