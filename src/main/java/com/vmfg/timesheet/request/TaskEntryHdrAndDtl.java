package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEntryHdrAndDtl {
	private String pmHdrId;
	private String categoryCode;
	private String typeCode;
	private String tenantId;
}
