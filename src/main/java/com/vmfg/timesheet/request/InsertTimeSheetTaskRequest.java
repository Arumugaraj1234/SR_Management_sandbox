package com.vmfg.timesheet.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertTimeSheetTaskRequest {
	private String taskGroupName;
	private String createdBy;
	private String updatedBy;
	private String tenantId;
	List<InsertTimeSheetTaskDtlRequest> timeSheetDtl;
}
