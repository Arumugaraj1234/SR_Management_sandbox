package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QulyProjCntRequest {
	private String tenantId;
	private String fromDate;
	private String toDate;
	private String empId;
	private String pmId;
}
