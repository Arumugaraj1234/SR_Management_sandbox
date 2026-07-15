package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TenantEmpRequest {
	private String tenantID;
	private String empId;
	private String isInternal;
}
