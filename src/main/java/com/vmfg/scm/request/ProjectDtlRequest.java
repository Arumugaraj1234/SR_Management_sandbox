package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ProjectDtlRequest {
	private String fromDate;
	private String toDate;
	private String tenantId;
	private String empId;

}
