package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PmHdrIdAndTenantIdRequest {

	private String pmHdrId;
	private String tenantId;
	private String getRetrunable;
	private String fromDate;
	private String toDate;
}
