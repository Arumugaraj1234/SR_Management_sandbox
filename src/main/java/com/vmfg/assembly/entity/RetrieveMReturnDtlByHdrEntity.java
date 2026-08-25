package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetrieveMReturnDtlByHdrEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String productId;
	private String productCode;
	private String productDesc;
	private String uomLongDesc;
	private String uomCode;
	private String station;
	private String subAssy;
	private String qty;
	private String mrdId;
	private String isApproved;
	private String specification;
	private String make;
	private String msHdrId;
	private String msName;
	private String costPerUnit;
	// Staging Group's own UOM (material_staging_hdr.UOM_CODE, set when the group was created) -
	// distinct from uomLongDesc above, which is each individual item's own product UOM. For a
	// group return, the group's own UOM should be shown, not derived/aggregated from its items.
	private String msUomLongDesc;
	// Staging Group's own Qty (material_staging_hdr.QTY, set when the group was created) -
	// distinct from qty above, which is each individual item's own returned quantity. For a
	// group return, the group's own Qty should be shown, not summed from its items.
	private String msQty;
	// Staging Group's own CREATED_ON/CREATED_BY (material_staging_hdr) - only
	// populated by retrieveApprovedGroupReturnsByProject, not the other queries
	// sharing this entity/row mapper (Inventory Master's Material Groups tab).
	private String msCreatedOn;
	private String msCreatedBy;
}
