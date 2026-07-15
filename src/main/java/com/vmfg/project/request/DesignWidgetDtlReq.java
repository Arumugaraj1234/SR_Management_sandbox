package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignWidgetDtlReq {
	private String monYr;
	private String empId;
	private String tenantId;
	private String deptCode;
    private String pmId;
    private String projId;
	private String lifeSpan;
}
