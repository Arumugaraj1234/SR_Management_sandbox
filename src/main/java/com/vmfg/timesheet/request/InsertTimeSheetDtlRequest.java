package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertTimeSheetDtlRequest {
	private String teDtlId;
	private String tdDtlId;
	private String timeSheetDtl;
	private String timeSheetHrs;
	private String tenantId;

}
