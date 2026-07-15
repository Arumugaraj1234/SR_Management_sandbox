package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertTimeSheetCategoryRequest {

	private String tcName;
	private String createdBy;
	private String updatedBy;
	private String tenantId;
}
