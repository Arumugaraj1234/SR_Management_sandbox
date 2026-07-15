package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagementProjRequest {

	private String pmHdrId;
	private String empId;
	private String customerId;
	private String vendorCode;
	private String stageCode;
	private String fromDate;
	private String toDate;
	private String tenantId;
	private String pmId;
}
