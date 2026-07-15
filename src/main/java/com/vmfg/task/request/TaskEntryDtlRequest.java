package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEntryDtlRequest {

	private String activityName;
	private String assignTo;
	private String ttDtlId;
	private String teDtlId;
	private String plannedStartDate;
	private String dueDate;
	private String tenantId;
	private String requirementFrom;
	private String qty;
}
