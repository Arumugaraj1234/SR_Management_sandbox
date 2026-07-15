package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMsDtlRequest {
	private String productId;
	private String qty;
	private String tenantId;
}
