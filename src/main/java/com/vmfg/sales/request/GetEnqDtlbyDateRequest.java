package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEnqDtlbyDateRequest {

	private String fromDate;
	private String toDate;
	private String customerName;
	private String tenantId;
	private String empId;
	private String tentativePoVal;
	private String isExpectedPoDate;
	private String slaveId;
}
