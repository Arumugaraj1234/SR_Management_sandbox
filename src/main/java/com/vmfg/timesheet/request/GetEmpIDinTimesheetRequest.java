package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEmpIDinTimesheetRequest {
	private String pmHdrId;
	private String empId;
	private String tenantId;
}
