package com.vmfg.mis.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PMMileRequest {

	private String month;
	private String yr;
	private String tenantId;
	private String pmHdrId;
}
