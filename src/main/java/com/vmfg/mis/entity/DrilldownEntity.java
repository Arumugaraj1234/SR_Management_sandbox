package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DrilldownEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String projectName;
	private String projectDescription;
	private String customerName;
	private String indentCode;
	private String deliveryDate;
	private String productCode;
	private String description;
	private String specification;
	private String make;
	private String pmHdrId;
	private String projectCode;
	private String indentDtlId;
	private String indentTypeCode;
	private String indentTypeDesc;
	private String assignedPerson;
	private String stage;
	private String qty;
	private String station;
	private String subAssy;
	private String type;
}
