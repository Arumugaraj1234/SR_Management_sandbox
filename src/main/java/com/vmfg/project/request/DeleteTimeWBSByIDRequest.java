package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTimeWBSByIDRequest {
	private String tenantID;
	private String ptID;

}
