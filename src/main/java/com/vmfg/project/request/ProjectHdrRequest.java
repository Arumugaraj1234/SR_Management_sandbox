package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectHdrRequest {
	private String tenantID;
	private String fromDate;
	private String toDate;
	private String custName;
	private String projectID;
	private String pmId;
	private String empId;

}
