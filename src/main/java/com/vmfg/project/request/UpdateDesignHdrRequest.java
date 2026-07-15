package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDesignHdrRequest {
	private String pmHdrId;
	private String tenantId;
	private String isStatus;
	private String pmId;
	private String mstId;
}
