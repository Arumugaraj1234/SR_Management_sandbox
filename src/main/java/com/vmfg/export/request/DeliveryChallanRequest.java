package com.vmfg.export.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeliveryChallanRequest {
	private String tenantId;
	private String key;
	private String dcId;

}
