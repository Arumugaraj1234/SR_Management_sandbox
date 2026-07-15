package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetVersionRequest {
	private String dmId;
	private String tenantId;

}
