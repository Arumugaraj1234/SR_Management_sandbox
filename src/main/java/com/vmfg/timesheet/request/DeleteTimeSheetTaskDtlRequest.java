package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTimeSheetTaskDtlRequest {
	private String tdDtlId;
	private String tdId;
	private String tenantId;
}
