package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectTimelineRequest {
	private String ptId;
	private String pmHdrId;
	private String pmTempId;
	private String milestoneName;
	private String responsibleName;
	private String responsibleDeptCode;
	private String plannedStartDate;
	private String plannedEndDate;
	private String tenantId;
	private String updatedBy;

}
