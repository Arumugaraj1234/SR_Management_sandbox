package com.vmfg.scm.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebitNoteHdrAndDtlRequest {

	private String pmHdrId;
	private String poId;
	private String vendorCode;
	private String dnrId;
	private String dnReason;
	private String dnValue;
	private String dnId;
	private String seq;
	private String seqStatus;
	private String isLast;
	private String remarks;
	private String empId;
	private String updatedBy;
	private String updatedDateTime;
	private String tenantId;
	private List<DebitNoteDtlRequest> debitNoteDtl;
}
