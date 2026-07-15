package com.vmfg.export.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IdAndTenantIdRequest {
	private String tenantId;
	private String key;
	private String poId;
	private String poType;

}
