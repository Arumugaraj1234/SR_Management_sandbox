package com.vmfg.timesheet.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeSheetDtlEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectName;
	private String pmHdrId;
	private String projectDesc;
	private String projectCode;
	private String empName;
	private String empId;
	private String empCode;
	private String recordedOn;
	private String timeSheetDtl;
	private String departmentDesc;
	private String departmentCode;
	private String timeSheetHrs;
	private String recordDate;
	private String tDtlId;
	private String timeSheetCategory;
	private String type;
	private String summary;
	private String category;
	private String taskEntryId;
	private String taskEntryDesc;
}
