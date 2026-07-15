package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvProdDrillEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pmHdrId;
	private String projectCode;
	private String productCode;
	private String productDesc;
	private String specification;      // SPECIFICATION
	private String make;               // MAKE
	private String uom;                // UOM_SHORT_DESCRIPTION
	private String weight;             // WEIGHT (Mass in Kgs)
	private String location;           // INVENTORY_LOCATION_DESCRIPTION
	private String bin;                // BIN
	private String qtyOnHand;          // PRODUCT_QUANTITY_ON_HAND
	private String costPerUnit;        // PRODUCT_COST_PER_UNIT
	private String inventoryValue;     // Qty × Unit Price
	private String inwardDateTime;     // INWARD_DATETIME
}
