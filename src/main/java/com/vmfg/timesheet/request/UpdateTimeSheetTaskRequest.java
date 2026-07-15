package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTimeSheetTaskRequest {
	private String tdDtlId;
	private String defaultTaskDtl;
	private String updatedBy;
	private String tenantId;
}
