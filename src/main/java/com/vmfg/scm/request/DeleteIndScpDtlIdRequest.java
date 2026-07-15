package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteIndScpDtlIdRequest {

	private String indScpId;
	private String empId;
	private String indentId;
	private String tenantId;
	private String mstId;
}
