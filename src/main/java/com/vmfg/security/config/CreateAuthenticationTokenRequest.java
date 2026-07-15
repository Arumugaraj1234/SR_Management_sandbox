package com.vmfg.security.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter	
public class CreateAuthenticationTokenRequest {

	private String userName;
	private String password;
}
