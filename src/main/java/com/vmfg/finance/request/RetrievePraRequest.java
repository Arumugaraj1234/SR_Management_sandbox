package com.vmfg.finance.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrievePraRequest {

	private String pmHdrId;
	private String poId;
	private String docTypeCode;
	private String empId;
	private String tenantId;
	
}
