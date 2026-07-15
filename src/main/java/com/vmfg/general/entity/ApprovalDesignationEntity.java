package com.vmfg.general.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vmfg.design.entity.GetIndentDtlEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApprovalDesignationEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dnAppId;
	private String dnDtlId;
	private String refId;
	private String docTypeCode;
	private String refCode;
	private String projectName;
	private String customerName;
	private String projectCode;
	private String docTypeDesc;
	private String insertedDate;
	private String pmId;
	private String projectId;
	private String mstId;
	private String enquiryId;
	private String isInternal;
	private String enquiryCode;
//	private String currentSeq;
//	private String isEditing;
//	private String targetCost;
//	private String actualCost;
	private List<GetIndentDtlEntity> indentDtl = new ArrayList<GetIndentDtlEntity>();
	
}
