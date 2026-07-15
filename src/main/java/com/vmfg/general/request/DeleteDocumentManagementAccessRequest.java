package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteDocumentManagementAccessRequest {
	private String dmaId;
	private String empId;
	private String tenantId;
}
