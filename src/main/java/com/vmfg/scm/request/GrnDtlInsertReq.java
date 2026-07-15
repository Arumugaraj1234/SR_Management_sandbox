package com.vmfg.scm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrnDtlInsertReq {
	private String recivedQty;
	private String indentDtlId;
	private String poDtlId;
	private String tenantId;
	private String pmHdrId;
	private String productCode;
	private String qty;
	private String uom;
	private String createdBy;
	private String dcDtlId;
	private String bin;
	private String productId;
}
