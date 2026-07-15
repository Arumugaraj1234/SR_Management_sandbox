package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter	
public class RoleMappingRequest {

	private String roleId;
	private String uiScreenMstId;
	private String tenantId;
	private int isActive;
}
