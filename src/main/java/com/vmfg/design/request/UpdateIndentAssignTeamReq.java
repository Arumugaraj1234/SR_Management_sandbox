package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateIndentAssignTeamReq {
	private String tenantId;
	private String indentId;
	private String employeeId;
	private String[] selectedIndents;

}
