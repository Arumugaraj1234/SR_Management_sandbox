package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSheetRequests {
	private String pmHdrId;
	private String fromDate;
	private String toDate;
	private String tenantId;
}
