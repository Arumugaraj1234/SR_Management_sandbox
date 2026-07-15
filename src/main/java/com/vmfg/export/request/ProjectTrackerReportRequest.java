package com.vmfg.export.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectTrackerReportRequest {
	
	private String projectId;
	private String tenantId;
	private String key;

}
