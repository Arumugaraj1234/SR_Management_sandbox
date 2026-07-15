package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTemplateHdrAndDtlReq {

	private String tenantId;
	private String ttHdrId;
	private String ttDtlId;
	
}
