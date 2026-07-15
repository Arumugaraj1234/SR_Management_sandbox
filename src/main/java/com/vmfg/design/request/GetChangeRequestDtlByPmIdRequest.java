package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetChangeRequestDtlByPmIdRequest {

	private String pmId;
	private String empId;
	private String tenantId;
}
