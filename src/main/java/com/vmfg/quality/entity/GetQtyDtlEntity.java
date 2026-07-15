package com.vmfg.quality.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetQtyDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String qhdrId;
	private String pmHdrId;
	private String initatedDate;
	private String transactionstageCode;
	private String transactionstageDesc;
	private String transactionstageseq;
	private String transactionstatusCode;
	private String hdrStatusDesc;
	private String transactionstatusseq;
	private String createdDatetime;
	private String lastUpdatedDatetime;
	private String tenantId;
	private String projectCode;
	private String projectName;
	private String projectDesc;
	private String customerName;
	private String qtyinspectionCompleted;
	private String qtyinspectionTotal;
	private String enquiryId;
	private String okCount;
	private String conditionalApprovedCnt;
	private String rejectedCount;
	private String reworkCount;
	private String qtyToBeInspected;
	private String qtyTotalCompleted;
	private String qtyDtlId;
}
