package com.vmfg.design.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetIndentLifecycDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String igHdrId;
	private String isInventroy;
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
	private String indentDtlId;
	private String productCode;
	private String productDesc;
	private String indentQty;
	private String poCheck;
	private String miCheck;
	private String grnCheck;
	private String inspCheck;
	private String poDateTime;
	private String miDateTime;
	private String grnDateTime;
	private String inspDateTime;
	private String groupCheck;
	private String pjsDateTime;
	private String revisionNo;
	private String revisionDate;
	private String praCheck;
	private String praDateTime;
	private String praReqCheck;
	private String praReqDateTime;
	private String inspReqCheck;
	private String inspReqDateTime;
	private String vendorName;
	private String deliveryDate;
	private String productId;
	private String typeCreatedTime;
	private String type;
	private List<IndentRemarksEntity> remarksval;
}
