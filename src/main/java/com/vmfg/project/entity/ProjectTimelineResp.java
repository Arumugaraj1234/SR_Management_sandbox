package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectTimelineResp implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ptId;
	private String pmHdrId;
	private String pmTempId;
	private String milestoneName;
	private String responsibleName;
	private String responsibleDeptCode;
	private String plannedStartDate;
	private String plannedEndDate;
	private String generatedStartDate;
	private String generatedEndDate;
	private String isInitiated;
	private String actualStartDate;
	private String actualEndDate;
	private String lastUpdatedDatetime;
	private String tenantId;
	private String deptName;
	private String empName;
}
