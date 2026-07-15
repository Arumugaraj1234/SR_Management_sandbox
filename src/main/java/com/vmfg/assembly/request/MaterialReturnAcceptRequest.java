package com.vmfg.assembly.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialReturnAcceptRequest {
	private String mrhId;
	private String empId;
	private String tenantId;
	private String currentSeq;
	private String isFinal;
	private String remarks;
	
}
