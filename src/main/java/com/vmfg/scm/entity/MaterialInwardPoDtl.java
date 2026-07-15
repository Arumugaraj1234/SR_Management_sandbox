package com.vmfg.scm.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialInwardPoDtl {
	private String poDtlId;
	private String indentDtlId;
	private String productCode;
	private String description;
	private String specification;
	private String weight;
	private String material;
	private String qty;
	private String uom;
	private String uomDesc;
	private String receivedQty;
	private String inspectedQty;
	private String inwardQty;
	private String inspectQty;
	private String tenantId;
	private String empId;
	private String dcDate;
	private String dcNo;
	private String pmId;
	private String invLocation;
	private String remarks;
	private String projectCode;
	private String poCode;
	private String pmHdrId;
	private String bin;
	private String qtyToInward;
	private String poqty;
	private String make;
	private String vendorCode;
	private String rejectedQty; // RejctedQty + ReworkQty
	private String waitingForQc; // qty to raise qc
	private String relationShipRating;
	private String totalValue;
	private String unitRate;

}
