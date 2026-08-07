package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertMrDtlReq {
	private String productId;
	private String qty;
	private String tenantId;
	private String msHdrId;
	private String msName;
}
