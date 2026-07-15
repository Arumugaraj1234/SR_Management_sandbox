package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignRequest {
	private String fromDate;
	private String toDate;
	private String customer;
	private String tenantID;
	private String empId;
	private String processId;
	private String designID;
	private String projectId;

}
