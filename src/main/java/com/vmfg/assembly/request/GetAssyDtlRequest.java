package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAssyDtlRequest {

	private String tenantID;
	private String fromDate;
	private String toDate;
	private String custName;
	private String assyId;
	private String pmId;
	private String empId;
	private String projectId;
}
