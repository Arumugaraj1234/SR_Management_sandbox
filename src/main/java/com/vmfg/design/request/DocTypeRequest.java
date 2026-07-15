package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocTypeRequest {
	private String tenantId;
	private String docTypeCode;
}
