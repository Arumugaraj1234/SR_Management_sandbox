package com.vmfg.task.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskTemplateDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ttDtlId;
	private String ttHdrId;
	private String activityName;
	private String plannedDurationDays;
	private String isActive;
	private String lastUpdatedtime;
	private String lastUpdatedBy;
	private String tenantId;
	private int sno;
}
