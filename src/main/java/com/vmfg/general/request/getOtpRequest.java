package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class getOtpRequest {
	private String userName;
	private String tenantId;
	private String otp;
}
