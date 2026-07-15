package com.vmfg.project.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProjectPlanDateRequest {

	private String pmHdrId;
	private String pmPlanDate;
	private String pmEndDate;
	private String tenantId;
	private String priority;
}
