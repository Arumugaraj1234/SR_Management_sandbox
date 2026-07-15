package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTemplateHdrNameRequest {

	private String typeCode;
	private String catCode;
	private String empId;
	private String tenantId;
}
