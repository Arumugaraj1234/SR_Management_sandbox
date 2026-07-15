package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String referenceDoc;
	private String referenceDocDesc;
	private String SeqNo;
	private String seqStatus;
	private String seqStatusDesc;
	private String empId;
	private String empDesc;
	private String updatedOn;	
	private String referenceId;
	private String tenantId;
	private String remarks;
}
