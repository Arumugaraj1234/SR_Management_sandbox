package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKeySubAreaByPKIdRequest {

	private String pmHdrId;
	private String tenantId;
	private String pkaId;
}
