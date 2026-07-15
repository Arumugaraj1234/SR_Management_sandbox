package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrnDtlEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	private String  grnDtlId;
	private String  miDtlId;
	private String  grnReceivedQty;
	private String  materialInwardReceivedQty;
	private String  miId;
	private String  poDtlId;
	private String  orderedQty;
	private String  inspectedQty;
	private String  uom;
	private String  indentDetailId;
	private String  indentId;
	private String 	productId;
	private String  productCode;
	private String  description;
	private String  specification;
	private String  make;
	private String  qty;
	private String  unit;
	private String  material;
	private String  remarks;
	private String  poCode;
	private String  dcCode;
	private String dcDate;
	private String mtlRemarks;
    private String uomDesc;


}
