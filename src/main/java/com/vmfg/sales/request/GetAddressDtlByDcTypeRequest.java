package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAddressDtlByDcTypeRequest {

	private String dcTypeCode;
	private String tenantId;
}
