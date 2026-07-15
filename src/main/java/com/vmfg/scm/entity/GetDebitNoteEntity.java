package com.vmfg.scm.entity;

import java.util.List;

import com.vmfg.finance.entity.GrnDtlsEntity;
import com.vmfg.general.entity.DocumentStatusMstEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetDebitNoteEntity {

	private String dnId;
	private String projectCode;
	private String projectName;
	private String poId;
	private String dmId;
	private String vendorName;
	private String vendorCode;
	private String reason;
	private String dnValue;
	private String seqno;
	private String seqStatus;
	private String seqDesc;
	private String billingName;
	private String billingAddressLine;
	private String billingCity;
	private String billingPincode;
	private String billingState;
	private String 	billingCount;
	private String billingGst;
	private String vendorAddressLine;
	private String vendorCity;
	private String vendorPincode;
	private String vendorGst;
	private String deliveryName;
	private String deliveryAddressLine;
	private String deliveryCity;
	private String deliveryPincode;
	private String deliveryState;
	private String deliveryCount;
	private String deliveryGst;
	private String deliveryContact;
	private String createdBy;
	private String tenantId;
	private String poCode;
	private String poType;
	private String poValue;
	private String poValueFx;
	private String isApproval;
	private List<DebitNoteDtlListEntity> debitNoteDtl;
	private List<DebitNoteStatusEntity> debitNoteStatus;
	private List<GrnDtlsEntity> grnDtlsEntity;
	private List<PoDtlEntity> poDtlEntity;
	private List<DocumentStatusMstEntity> docStatusMst;
}
