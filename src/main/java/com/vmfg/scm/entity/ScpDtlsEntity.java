package com.vmfg.scm.entity;

import java.io.Serializable;
import java.util.List;

import com.vmfg.general.entity.DocumentStatusMstEntity;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ScpDtlsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String igScpId;
	private String igHdrId;
	private String technicalCompassion;
	private String technicalRecommendation;
	private String vendorEvaluated;
	private String vendorShortListed;
	private String justification;
	private String vendorQualified;
	private String customerApproval;
	private String createdDate;
	private String createdBy;
	private String seqNo;
	private String seqStatus;
	private String seqStatusDesc;
	private String isApproved;
	private String tenantId;
	private String indentId;
	private String isEditable;
	private String type;
    private String mstId;
    private String pmId;
	private String l1CurrencyType;
	private String l2CurrencyType;
	private String l3CurrencyType;

	private String l1ExchangeRate;
	private String l2ExchangeRate;
	private String l3ExchangeRate;
	private PoCancelEntity poCancel;
	private List<IndentGrpScpDtlEntity> scpDtlList;
	private List<IndentGrpScpVenEntity> scpVendorList;
	private List<IndentGrpScpVenDtlEntity> scpVendorDtlList;
	private List<IndentGrpScpVenPtEntity> scpvendorPtList;
	private List<IndentGrpScsStatusEntity> scsStatusList;
	private List<DocumentStatusMstEntity> docLifeCycleMstList;

}
