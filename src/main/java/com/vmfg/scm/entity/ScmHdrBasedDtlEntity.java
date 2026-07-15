package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmHdrBasedDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String scmHdrId;
	private String pmHdrId;
	private String scmInitiatedDate;
	private String poCount;
	private String intentCount;
	private String projectCode;
	private String transactionNo;
	private String transactionStatus;
	private String transactionStatusSeq;
	private String hdrStatusDesc;
	private String sNo;
	private String customerName;
	private String projectName;
	private String dueDate;
	private String inwardCount;
	private String grnCount;
	private String enquiryId;
	private String isInternal;
}
