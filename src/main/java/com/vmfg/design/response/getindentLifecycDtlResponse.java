package com.vmfg.design.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getindentLifecycDtlResponse {

	private String projectId;
	private String indentType;
	private String tenantId;
	private String indentId;
	private String empId;
	private String fromDate;
	private String toDate;
}
