package com.vmfg.master.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertFileUploadConfigRequest {

	private String fuCode;
	private String desc;
	private String descCode;
	private String tenantId;
}
