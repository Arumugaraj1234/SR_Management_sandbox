package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class IndentDtlRequest {
	private String indentId;
	private String tenantId;
	private String empId;
	private String projectId;
	private String pmId;
	private String byProjectId;
	private String docType;
	private String depCode;

}
