package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter	
public class EmployeeTenantRequest {

	private String tenantId;
	private String isActive;
	private String employeID;
	
	
}
