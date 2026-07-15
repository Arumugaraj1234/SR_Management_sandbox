package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IdAndTenantIdRequest {
	private String hdrId;
	private String tenantId;
	private String empId;
	private String projectId;
    private String processCode;
}
