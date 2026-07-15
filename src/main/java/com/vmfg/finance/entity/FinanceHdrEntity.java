package com.vmfg.finance.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FinanceHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String feHdrId;
	private String pmHdrId;
	private String projectCode;
	private String projectName;
	private String projectDescription;
	private String productDetails;
	private String customerName;
	private String requestedBy;
	private String initiatedDate;
	private String transactionStage;
	private String hdrStatusDesc;
	private String handoverDate;
	private String dueDate;
	private String enquiryId;
	private String isInternal;
}
