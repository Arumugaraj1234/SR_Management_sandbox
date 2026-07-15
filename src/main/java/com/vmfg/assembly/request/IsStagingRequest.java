package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsStagingRequest {

	private String hdrId;
	private String isQc;
	private String tenantId;
}
