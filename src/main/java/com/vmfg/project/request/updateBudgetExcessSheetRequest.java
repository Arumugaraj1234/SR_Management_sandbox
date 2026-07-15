package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class updateBudgetExcessSheetRequest {
	
	private String beHdrId;
	private String reason;
	private String rootCase;
	private String action;
	private String responseDept;
	private String approvingStatus;
	private String sequenceNo;
	private String empId;
	private String remarks;
	private String tenantId;
}
