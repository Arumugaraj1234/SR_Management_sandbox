package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeSheetDtlRequest {
	private String fromDate;
	private String toDate;
	private String logginEmpId;
	private String unLogginEmpId;
	private String pmHdrId;
	private String tenantId;
}
