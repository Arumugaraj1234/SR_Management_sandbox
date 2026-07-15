package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class insertUpdateTaskTypeRequest {

	private String ttcode;
	private String ttdesc;
	private String deptCode;
	private String tenantId;
	private String isActive;
}
