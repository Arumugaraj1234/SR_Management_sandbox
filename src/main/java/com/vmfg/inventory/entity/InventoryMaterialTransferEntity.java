package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryMaterialTransferEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mtrId;
	private String productId;
	private String fromPmHdrId;
	private String toPmHdrId;
	private String transferQuantity;
	private String transferDate;
	private String createdOn;
	private String createdBy;
	private String reason;
	private String frmcustomerName;
	private String frmprojectCode;
	private String frmprojectName;
	private String tocustomerName;
	private String toprojectCode;
	private String toprojectName;
	private String frmLocation;
	private String toLocation;
	private String productCode;
	private String productDesc;
	private String uom;
	private String fromLocationDesc;
	private String toLocationDesc;
	private String sNo;
	private String referenceId;
	private String overAllCost;
	private String unitCost;
	private String fromBin;
	private String toBin;
	

}
