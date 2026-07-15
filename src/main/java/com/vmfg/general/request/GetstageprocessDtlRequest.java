package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetstageprocessDtlRequest {

	private String referenceId;
	private String processDoc;
	private String tenantId;
	private String docDesc;
}
