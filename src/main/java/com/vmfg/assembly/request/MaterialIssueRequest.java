package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialIssueRequest {
	private String pmHdrId;
	private String tenantId;
	private String fromName;
}
