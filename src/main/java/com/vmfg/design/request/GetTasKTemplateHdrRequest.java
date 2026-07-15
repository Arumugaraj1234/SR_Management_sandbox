package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetTasKTemplateHdrRequest {

	private String ttCode;
	private String tcCode;
	private String tenantId;
}
