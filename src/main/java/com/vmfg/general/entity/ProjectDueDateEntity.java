package com.vmfg.general.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProjectDueDateEntity {
	private String pdId;
	private String pmHdrId;
	private String dueDate;
	private String reason;
	private String updatedBy;
	private String updatedOn;
	private String tenantId;

}
