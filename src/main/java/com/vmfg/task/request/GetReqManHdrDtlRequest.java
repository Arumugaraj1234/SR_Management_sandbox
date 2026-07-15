package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetReqManHdrDtlRequest {
	private String pmHdrId;
	private String tenantId;
}
