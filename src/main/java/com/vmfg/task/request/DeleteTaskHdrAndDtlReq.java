package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTaskHdrAndDtlReq {
	private String teDtlId;
	private String tenantId;
}
