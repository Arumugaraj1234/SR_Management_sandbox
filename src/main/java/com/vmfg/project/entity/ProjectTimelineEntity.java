package com.vmfg.project.entity;

import java.io.Serializable;
import java.util.List;

import com.vmfg.task.entity.GetTaskEntryDtlEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectTimelineEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String milestoneName;
	private String plannedStartDate;
	private String plannedEndDate;
	private String employeeName;
	private String employeeId;
	private String departName;
	private String departmentCode;
	private String actualStartDate;
	private String actualEndDate;
	List<GetTaskEntryDtlEntity> taskEntryDtlEntity;
}
