package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetKeyAreaDtlsRequest {
	private String productCode;
	private String tenantId;
	private String masterId;
	private String indentId;

}
