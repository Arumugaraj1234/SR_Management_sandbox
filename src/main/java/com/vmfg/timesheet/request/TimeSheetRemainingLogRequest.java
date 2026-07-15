package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSheetRemainingLogRequest {

	private String empId;
	private String date;
	private String tenantId;
}
