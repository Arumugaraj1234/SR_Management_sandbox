package com.vmfg.timesheet.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTimeSheetCategoryRequest {
	private String tenantId;
	private String tcId;
}
