package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrieveQualitInspectionReq {
	private String projectId;
	private String tenantId;
	private String empId;
	private String pmId;
}
