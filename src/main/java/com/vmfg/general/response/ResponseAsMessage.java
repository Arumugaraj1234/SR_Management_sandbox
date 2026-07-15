package com.vmfg.general.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseAsMessage {

	String responseCode;
	String responseMessage;
	String responseDataMessage;
}
