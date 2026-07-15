package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTaskCompPerEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String rtId;
	private String employeeId;
	private String pmHdrId;
	private String departmentCode;
	private String reportyear;
	private String reportmonth;
	private String weekStart;
	private String reportDate;
	private String noPlannedTask;
	private String noCompltedTask;
	private String delayTask;
	private String percentageCompleted;
	private String tenantId;
	private String employeeName;
}
