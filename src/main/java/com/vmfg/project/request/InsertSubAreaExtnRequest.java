package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertSubAreaExtnRequest {

	private String pkaId;
	private String sbExtnId;
	private String allocatedQty;
	private String allocatedvalue;
	private String tenantId;
	private String pmId;
}
