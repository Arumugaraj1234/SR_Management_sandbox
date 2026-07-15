package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentDtlDeleteRequest {
	private String indentDtlId;
	private String dmId;
	private String tenantId;
}
