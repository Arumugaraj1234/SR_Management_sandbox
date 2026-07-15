package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDcDtlByDcIdRequest {

	private String dcHdrId;
	private String tenantId;
	private String empId;
	private String hdrId;
}
