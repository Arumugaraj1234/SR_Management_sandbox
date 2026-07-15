package com.vmfg.export.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DcRequestDtlEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String dcDtlId;
	private String dcrId;
	private String productId;
	private String descofGoods;
	private String productCode;
	private String qty;
	private String closedQty;
	private String pendingQty;
    private String mrHdrId;
}
