package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignReportMisRequest {
	
	private String monthYear;
	private String empId;
	private String tenantId;
	private String deptCode;
	private String category;
	private String pmId;
	private String projectId;
}
