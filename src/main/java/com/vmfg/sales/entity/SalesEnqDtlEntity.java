package com.vmfg.sales.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesEnqDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String hdrStatusDesc;
	private String slaveStatusDesc;
	private String stgDesc;
	private String slaveStatus;
	private String seId;
	private String enquiryCode;
	private String enquiryNo;
	private String projectName;
	private String customerName;
	private String industrialType;
	private String scopeOfWork;
	private String projectDescription;
	private String productDetails;
	private String enquiryType;
	private String enquiryCustomerStatus;
	private String enquiryDate;
	private String reason;
	private String leadDtl;
	private String tentativePoValue;
	private String transactionStatus;
	private String transactionStatusSeq;
	private String transactionStage;
	private String transactionStageSeq;
	private String location;
	private String createdDateTime;
	private String lastUpdatedDatetime;
	private String tenantId;
	private String tentativePoMonth;
//	private String industrialDesc;
//	private String scopeOfWorkDesc;
	private String expectedPoDate;
	private String finalCost;
	private String employeeNames;
	private List<SalesEnqContactEntity> salesContact;
	private List<CustomerMstEntity> custMstEntity;
	
	
}
