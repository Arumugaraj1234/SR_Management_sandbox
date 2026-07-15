package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DelayedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String indentDtlId;
	private String revesionNumber;
	private String revisionDate;
	private String indentCode;
	private String indentTypeCode;
	private String createdById;
	private String createdDateTime;
	private String sequenceNumber;
	private String expectedDeliveryDate;
	private String customerName;
	private String projectName;
	private String projectDescription;
	private String indentTypeDesc;
	private String employeeName;
	private String projectCode;
//	private String indentDtlId;
	private String description;
	private String make;
	private String material;
	private String productCode;
	private String qty;
	private String remarks;
	private String specification;
	private String unit;
	private String weight;
	private String station;
	private String subAssy;
}
