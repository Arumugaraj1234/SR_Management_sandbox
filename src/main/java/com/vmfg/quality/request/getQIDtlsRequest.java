package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class getQIDtlsRequest {
	private String qiId;
	private String tenantId;
	private String qicName;
	private String empId;
}
