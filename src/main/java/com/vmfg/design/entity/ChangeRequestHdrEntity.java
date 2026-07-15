package com.vmfg.design.entity;

import java.io.Serializable;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRequestHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String crId;
	private String crNO;
	private String crDate;
	private String productCode;
	private String productName;
	private String deHdrId;
	private String pmHdrId;
	private String pmHdrCode;
	private String pmHdrName;
	private String initiatedBy;
	private String initiatedByDesc;
	private String pkId;
	private String pkdesc;
	private String pskId;
	private String pskDesc;
	private String requestDetails;
	private String nextApprovingDesig;
	private String nextApprovingDesigDesc;
	private String isCompleted;
	private String isApproved;
	private String transactionStatus;
	private String transactionStatusSeq;
	private String transactionStatusDesc;
	private String updatedDrawingNo;
	private String updatedDrawingRevNo;
	private String approvedOn;
	private String createdBy;
	private String createdBydesc;
	private String createdOn;
	private String lastUpdatedBy;
	private String tenantId;
	private String lastUpdatedOn;
	private String lastUdpdateByDesc;
	private String productDesc;
	private String dmId;
	private List<ChangeRequestDtlEntity> crDtlList; 
	List<DocumentStatusMstEntity> docLifeCycleMstList;
}
