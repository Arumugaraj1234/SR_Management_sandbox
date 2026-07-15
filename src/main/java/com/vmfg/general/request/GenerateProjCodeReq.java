package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenerateProjCodeReq {
	private String pmHdrId;
	private String pmId;
	private String tenantId;
	private String projectCode;
}
