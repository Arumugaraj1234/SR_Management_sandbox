package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertIndividualTimeSheetTaskDtl {
	private String tdId;
	private String defaultTaskDtl;
	private String tenantId;
}
