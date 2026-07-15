package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberLoadReq {

	private String projId;
	private String teamMemEmpId;
	private String empId;
	private String tenantID;
	private String pmId;
	private String fromDate;
	private String toDate;
}
