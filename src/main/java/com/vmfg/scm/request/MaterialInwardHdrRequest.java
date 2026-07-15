package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialInwardHdrRequest {
	private String poId;
	private String tenantId;
	private String pmHdrId;
	private String fromDate;
	private String toDate;
}
