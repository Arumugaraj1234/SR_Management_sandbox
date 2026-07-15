package com.vmfg.design.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class IndentHdrDtlsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String indentId;
	private String indentCode;
	private String projectName;
	private int noOfProductsCount;
	private String createdOn;
	private String createdBy;
	private String statusDesc;
	private String sbcDesc;
	private String keyAreaDesc;
	private String keyAreaId;
	private String subKeyAreaDesc;
	private String indentTypeDesc;
	private String expectedDeliveryDate;
	private String targetCost;
	private String statusSeq;
	private String indentClosed;
	private String indentClosedDate;
	private int isFlag;
	private String revisionNo;
	private String revisionOn;
	private String createdUserId;
	private String nextstatusDesc;
	private int verCheck;
	private boolean isAssigned;
	private List<IndentRemarksEntity> remarksval;
	List<GetindentDtlcycEntity> dtl;
}
