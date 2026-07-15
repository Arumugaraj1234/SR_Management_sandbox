package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetriveBudgetExcessStatusDtlEntity implements Serializable{

	private static final long serialVersionUID = 1L;


	private String beHdrId;
	private String sequenceNo;
	private String sequenceStatus;
	private String sequenceStatusDesc;
	private String remarks;
	private String updatedBy;
	private String empName;
	private String tenantId;
	private String updatedOn;
	private String besId;


}
