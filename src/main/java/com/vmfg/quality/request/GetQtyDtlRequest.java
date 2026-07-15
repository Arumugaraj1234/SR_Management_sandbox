package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetQtyDtlRequest {

	private String fromDate;
	private String toDate;
	private String customerName;
	private String tenantId;
	private String empId;
	private String qHdrId;
	private String pmId;
	private String projectId;
}
