package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductMasterEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String productCode;
	private String productUomCode;
	private String productDescription;
	private String tenantId;
	private String isActive;
	private String createdUserId;
	private String createdDateTime;
	private String lastUpdatedUserId;
	private String lastUpdatedDateTime;
	private String isInventory;
	private String pkaId;
	private String psksId;
	private String productReorderLevel;
	private String minimumOrderLevel;
	private String safetyStock;
	private String safetyStockDays;
	private String productHsnSacCode;
	private String productCostPerUnit;
	private String productGstTaxRate;
	private String specification;
	private String make;
	private String qty;
	private String unit;
	private String weight;
	private String material;
	private String productCategory;
	private String bin;
	private String inwardDateTime;
}
