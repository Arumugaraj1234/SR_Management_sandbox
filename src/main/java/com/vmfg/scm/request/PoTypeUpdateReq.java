package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PoTypeUpdateReq {
	private String poType;
	private String poId;
	private String empId;
}
