package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getDCProductDropDownRequest {

	private String pmHdrId;
	private String tenantId;
	private String fromName;
}
