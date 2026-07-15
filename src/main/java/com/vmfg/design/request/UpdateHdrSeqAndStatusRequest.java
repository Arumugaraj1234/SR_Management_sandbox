package com.vmfg.design.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateHdrSeqAndStatusRequest {
	private String currentseq;
	private String empId;
	private String tenantId;
	private String hdrId;

}
