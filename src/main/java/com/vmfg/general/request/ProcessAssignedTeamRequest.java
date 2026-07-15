package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter	
public class ProcessAssignedTeamRequest {
	private String referenceId;
	private String employeeID;
	private String tenantId;
	private String referenceDoc;
	private String projectId;
}
