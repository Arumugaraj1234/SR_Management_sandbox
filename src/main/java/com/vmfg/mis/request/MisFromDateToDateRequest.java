package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MisFromDateToDateRequest {

	private String fromDate;
	private String toDate;
	private String tenantId;
	private String projectId;
	private String empId;
	private String pmId;
	private String depCode;
}
