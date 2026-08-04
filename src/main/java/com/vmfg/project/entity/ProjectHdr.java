package com.vmfg.project.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectHdr implements Serializable {

	private static final long serialVersionUID = 1L;
	private int sno;
	private String enquiryCode;
	private String pmHdrId;
	private String projCode;
	private String projectName;
	private String projDec;
	private String enquiryId;
	private String productDtl;
	private String createdDate;
	private String dueDate;
	private String customerName;
	private String stagName;
	private String statusType;
	private String hdrStatusDesc;
	private String indentPlan;
	private String indentActual;
	private String initiateDate;
	private String plannedStartDate;
	private String plannedEndDate;
	private String scopeOfWork;
	private String industrialType;
	private String priority;
	private String saleValue;
	private String projHandOverDate;
	private String completionPercent;
	private String targetCost;
	private String sbHdrId;
	private String debitValue;
	private String isInternal;
	private List<BudgetSheetPaymentEntity> paymentTerms;

	private String costFlowType;
	private String allocatedValue;
	private String actualSpent;
}
