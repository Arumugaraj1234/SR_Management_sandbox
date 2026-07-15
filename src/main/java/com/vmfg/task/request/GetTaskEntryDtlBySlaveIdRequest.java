package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskEntryDtlBySlaveIdRequest {

	private String tenantId;
	private String typeCode;
	private String masterId;
	private String categoryCode;
	private String empId;
}
