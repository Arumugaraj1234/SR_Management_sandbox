package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class taskTemplateHdrRequest {

	private String templateName;
	private String empId;
	private String deptCode;
	private String ttCode;
	private String tcCode;
	private String tenantId;
	private String actName;
	private String isActive;
}
