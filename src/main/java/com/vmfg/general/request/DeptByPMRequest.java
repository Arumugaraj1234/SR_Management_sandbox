package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeptByPMRequest {

	private String pmId;
	private String tenantId;
	private String deptCode;

}
