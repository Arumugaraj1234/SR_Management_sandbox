package com.vmfg.export.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcReqDtlRequest {

	private String productId;
	private String descofGoods;
	private String qty;
	private String closedQty;
	private String tenantId;
}
