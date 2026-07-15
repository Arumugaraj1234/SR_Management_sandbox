package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeDtlByHdrRequest {
	private String thdrId;
	private String tenantId;
}
