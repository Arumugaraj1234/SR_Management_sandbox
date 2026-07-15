package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String dcDtlId;
	private String dcId;
	private String descOfGoods;
	private String hsnNo;
	private String qty;
	private String uom;
	private String rate;
	private String total;
	private String uomDesc;
	private String productId;
	private String receivedQty;
	private String productCode;
	private String mrHdrId;
	private String bin;
	private String hdrId;
	private String indentDtlId;
	private String locationCode;
}
