package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialIssueHdrRequest {
	private String hdrId;
	private String productId;
	private String tenantId;
}
