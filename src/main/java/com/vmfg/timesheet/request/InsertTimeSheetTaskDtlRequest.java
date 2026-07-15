package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertTimeSheetTaskDtlRequest {
	private String defaultTaskDtl;
	private String tenantId;
}
