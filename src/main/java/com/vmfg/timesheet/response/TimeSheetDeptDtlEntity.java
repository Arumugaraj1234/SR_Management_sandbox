package com.vmfg.timesheet.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeSheetDeptDtlEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String departmentCode;
	private String departmentName;
	private String hours;
	private String rupees;
	private String percentageOfHrs;
	private String percentageOfRupees;
}
