package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCategoryInsertUpdateRequest {
	
	private String tcCode;
	private String ttCode;
	private String tcDesc;
	private String tenantId;
	private String isActive;

}
