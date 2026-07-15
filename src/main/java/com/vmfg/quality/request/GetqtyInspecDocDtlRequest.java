package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetqtyInspecDocDtlRequest {

	private String enqId;
	private String pmHdrId;
	private String stgCode;
	private String docTypeCode;
	private String uploadDocType;
	private String refId;
	private String tenantId;
}
