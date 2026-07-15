package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTimeSheetTaskHdrGroupName {
	private String tdId;
	private String taskGroupName;
	private String tenantId;
}
