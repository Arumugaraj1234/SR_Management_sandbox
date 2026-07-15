package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAssyDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String assyHdrId;
	private String pmHdrId;
	private String projectName;
	private String customerName;
	private String projectDesc;
	private String productDetails;
	private String requestDate;
	private String requestBy;
	private String planStartDate;
	private String planEndDate;
	private String actualStartDate;
	private String actualEndDate;
	private String startMaterialReq;
	private String transactionstatus;
	private String hdrStatusDesc;
	private String transactionstatusSeq;
	private String transactionStage;
	private String transactionStageSeq;
	private String transactionStageDesc;
	private String createdDateTime;
	private String lastUpdatedDateTime;
	private String tenantId;
	private String indentCount;
	private String indentIsCompletedCount;
	private String materialRequestHdrCount;
	private String materialRequestIsCompletedCount;
	private String projectCode;
	private String enquiryId;
	private String isInternal;
	
}
