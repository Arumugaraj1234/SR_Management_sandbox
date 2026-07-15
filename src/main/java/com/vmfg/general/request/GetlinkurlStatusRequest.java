package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetlinkurlStatusRequest {

	private String roleId;
	private String linkUrl;
	private String tenantId;
}
