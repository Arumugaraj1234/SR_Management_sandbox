package com.vmfg.task.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskDtlSeqRequest {

	private String dtlId;
	private String tenantId;
	private String seq;
	private String status;
	private String isLastSeq;
	private String empId;
	private String remarks;
	private String docTypeCode;
	private String isFlag;
	private String mstId;
	private String pmId;
	private String pmHdrId;
	private String EnquiryID;
}
