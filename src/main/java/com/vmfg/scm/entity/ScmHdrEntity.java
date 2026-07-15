package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScmHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String scmHdrId;
	private String pmHdrId;
	private String scmInitiatedDate;
	private String sNo;
	private String dueDate;
	private String transactionStatus;
	private String transactionStatusSeq;
	private String hdrStatusDesc;
	private String enquiryId;
	private String isInternal;
}
