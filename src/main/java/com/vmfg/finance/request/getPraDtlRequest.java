package com.vmfg.finance.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getPraDtlRequest {

	private String praId;
	private String tenantId;
	private String empId;
}
