package com.vmfg.export.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcRequestHdrRequest {
	
	private String requestedBy;
	private String pmHdrId;
	private String remarks;
	private String isCompleted;
	private String tenantId;
	private String mrHdrId;
	
	private List<DcReqDtlRequest> dcreqdtl;

}
