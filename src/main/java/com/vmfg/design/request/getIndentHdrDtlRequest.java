package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class getIndentHdrDtlRequest {
	private String projectId;
	private String tenantId;
	private String empId;
	private String docType;
}
