package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectApprovalRequest {

	private String tenantId;
	private String hdrId;
	private String empId;
	private String remarks;
	private String currentseq;
	private String pmId;
	private String docType;
	private String pmHdrId;
	private String mstId;
	private String enquiryId;
	private String docGroup;
	private String poId;
}
