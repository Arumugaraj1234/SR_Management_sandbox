package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoOfPoDrillEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String indentDtlId;
	private String description;
	private String indentId;
	private String make;
	private String material;
	private String productCode;
	private String qty;
	private String remarks;
	private String specification;
	private String unit;
	private String weight;
	private String basicTotal;
	private String totalValue;
	private String indentCode;
	private String expectedDeliveryDate;
	private String poNumber;
	private String vendor;
	private String poDate;
	private String projectId;
	private String projectCode;
	private String indentTypeDesc;
	private String subAssy;
	private String station;
	private String venCategory;
	private String pjsPoCreatorName;
}
