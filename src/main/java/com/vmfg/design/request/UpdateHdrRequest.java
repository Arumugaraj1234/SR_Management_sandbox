package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UpdateHdrRequest {
	private String currentseq;
	private String empId;
	private String tenantId;
	private String indentId;
	private String remarks;
	private String pmId;
	private String masterId;
	private String docType;

}
