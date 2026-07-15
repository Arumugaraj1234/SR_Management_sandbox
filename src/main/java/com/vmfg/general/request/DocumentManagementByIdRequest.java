package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentManagementByIdRequest {

	private String refId;
	private String tenantId;
	private String stgCode;

}
