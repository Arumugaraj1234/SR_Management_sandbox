package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class QiCaDtlsRequest {
	private String qiCaDtlId;
	private String tenantId;
	private String empId;
	private String pmId;
}
