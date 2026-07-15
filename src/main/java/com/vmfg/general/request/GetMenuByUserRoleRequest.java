package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMenuByUserRoleRequest {

	private String loginId;
	private String tenantId;
	private String userRoleId;
}
