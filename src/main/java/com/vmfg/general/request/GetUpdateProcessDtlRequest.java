package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUpdateProcessDtlRequest {

	private String referenceId;
	private String referenceDoc;
	private String updatedSeq;
	private String lastSeq;
	private String processCode;
	private String tenantId;
	private String empId;
	private String remarks;
	private String pmId;
}
