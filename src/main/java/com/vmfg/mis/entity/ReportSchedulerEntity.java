package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportSchedulerEntity implements Serializable {

	private static final long serialVersionUID = 1L;
    
	private String empId;
	private String projId;
	private String deptCode;
	private String year;
	private String month;
	private String dayStart;
	private String noPlanned;
	private String noCompleted;
	private String delay;
	private String perCentage;
	private String tenantID;
}
