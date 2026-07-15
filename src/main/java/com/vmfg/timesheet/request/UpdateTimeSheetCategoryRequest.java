package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTimeSheetCategoryRequest {
	private String tcId;
	private String tcName;
	private String tenantId;
	private String updatedBy;

}
