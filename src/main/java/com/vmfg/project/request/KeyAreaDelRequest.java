package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyAreaDelRequest {

	private String pkaId;
	private String tenantId;
	private String projectid;
	private String pmId;
}
