package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PMInvReq {
	private String tenantId;
	private String projectId;
	private String invLocationCode;
}
