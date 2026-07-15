package com.vmfg.project.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSubAreaPmHdrListRequest {

	private String pmHdrId;
	private String pkId;
	private String pkaId;
	private String tenantId;
}
