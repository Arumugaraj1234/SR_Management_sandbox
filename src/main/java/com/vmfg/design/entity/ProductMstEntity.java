package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String productCode;
	private String productDesc;
	private String productId;
	private String pmHdrId;
	private String projectCode;
	private String uomCode;
	private String uomLongDescriprtion;
	private String uomShortDescriprtion;
	private String pkaId;
	private String pskaId;
	private String make;
	private String qty;
	private String unit;
	private String weight;
	private String material;
	private String lastupdatedUserId;
	private String lastupdatedDateTime;
	private String pkDesc;
	private String pskDesc;
	private String serialNumber;
	private String specification;
	private String indentCode;
	private String sbcDesc;
	private String indentDtlId;
	private int dmId;
	private String remarks;
	private String poCode;
	private String vendorName;
	private String unitRate;
	private String totalValue;
	private String fileNameExtn;
	private String isPdf;
	
}
