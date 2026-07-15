package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeSheetTaskForHdrandDtlRequset {
	private String tdId;
	private String tenantId;
}
