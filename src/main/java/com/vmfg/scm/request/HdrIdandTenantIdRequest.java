package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HdrIdandTenantIdRequest {
	private String hdrId;
	private String tenantId;
	private String projectCode;
	private String processCode;
 
}
