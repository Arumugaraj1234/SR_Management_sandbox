package com.vmfg.quality.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetConfigNameReq {
	private String tenantId;
	private String qiId;
}
