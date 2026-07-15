package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteActivityforEmpIdRequest {
	private String tdtlId;
	private String tenantId;
}
