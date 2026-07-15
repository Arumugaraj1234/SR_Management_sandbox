package com.vmfg.assembly.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getAssyDtlResponse {

	private String tenantID;
	private String fromDate;
	private String toDate;
	private String custName;
	private String assyId;
	private String pmId;
	private String empId;
}
