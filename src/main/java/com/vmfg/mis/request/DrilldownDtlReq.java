package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrilldownDtlReq {

	private String projId;
	private String tenantId;
	private String typeCode;
	private String fromDate;
	private String toDate;
	private String empId;
	private String pmId;
}
