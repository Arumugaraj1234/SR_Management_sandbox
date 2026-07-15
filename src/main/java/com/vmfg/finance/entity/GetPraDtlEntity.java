package com.vmfg.finance.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPraDtlEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String praId;
	private String praCode;
	private String poId;
	private String poDate;
	private String potId;
	private String paymentTerms;
	private String praDate;
	private String pmHdrId;
	private String dueDate;
	private String typeOfPayment;
	private String deliveryType;
	private String vendorCode;
	private String grnHdrId;
	private String invoiceNumber;
	private String invoiceDate;
	private String orderValue;
	private String invoiceValue;
	private String tds;
	private String amountPayable;
	private String retention;
	private String ld;
	private String others;
	private String status;
	private String isCompleted;
	private String completedDateTime;
	private String lastUpdatedDateTime;
	private String lastUpdatedOn;
	private String remarks;
	private String tenantId;
	private String poCode;
	private String revision;
	private String revisionDate;
	private String vendorName;
	private String statusDesc;
	private String isApproval;
	private String projectCode;
	private String projectName;
	private String poQty;	 
	private String poValue;
	private String poType;
	private String gst;
	private String basicTotal;
	private String isEditable;
	private String poCostType;
	private String verCheck;
	private String insuranceValue;
	private String transportValue;
	private String pfValue;
	private String otherValue;
	private String praGst;
	List<PraDtlListEntity> dtl =new ArrayList<PraDtlListEntity>();
	List<GrnDtlsEntity> grnDtl=new ArrayList<GrnDtlsEntity>();
	List<PraDtlsHistoryEntity> getPraDtl=new ArrayList<PraDtlsHistoryEntity>();
	private List<DocumentStatusMstEntity> docStatusMst;
	private List<PraStatusEntity> praStatusList;
}
