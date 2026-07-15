package com.vmfg.timesheet.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertTimeSheetRequest {
	private String tcId;
	private String empId;
	private String recordDate;
	private String teHdrId;
	private String pmHdrId;
	private String tdId;
	private String summary;
	private String recordedBy;
	private String tenantId;
	private String taskCategoryCode;
	private List<InsertTimeSheetDtlRequest> timeSheetDtl;
}
