package com.vmfg.timesheet.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSheetRequestsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectCode;
	private String projectName;
	private String recordDate;
	private String activity;
	private String employeeCode;
	private String employeeName;
	private String hrs;
	private String rupees;
	private String timeSheetDtlId;
    private String summary;
}
