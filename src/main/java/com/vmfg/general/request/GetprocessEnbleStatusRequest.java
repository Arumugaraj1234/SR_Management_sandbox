package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetprocessEnbleStatusRequest {

	private String slaveId;
	private String referenceDocType;
	private String tenantId;
	private String empId;
	private String referenceId;
}
