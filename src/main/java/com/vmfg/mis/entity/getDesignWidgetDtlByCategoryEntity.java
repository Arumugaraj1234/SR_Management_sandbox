package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getDesignWidgetDtlByCategoryEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String activityName;
	private String plannedStartDate;
	private String plannedCompletedDate;
	private String assignedToId;
	private String assignedToName;
	private String completedDate;
	private String projNum;
	private String projName;
}
