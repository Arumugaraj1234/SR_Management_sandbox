package com.vmfg.finance.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UdpatePraHdrSeqRequest {

	private String seq;
	private String seqDesc;
	private String islast;
	private String tenantId;
	private String praId;
	private String pmHdrId;
	private String processCode;
	private String enqId;
	private String praCode;
	private String poNo;
	private String empId;
	private String remarks;
	private String isPrevStage;
}
