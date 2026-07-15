package com.vmfg.sales.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class getFileConfigDtlRequest {

	private String documentTypeCode;
	private String tenantId;
	private String enquiryId;

}
