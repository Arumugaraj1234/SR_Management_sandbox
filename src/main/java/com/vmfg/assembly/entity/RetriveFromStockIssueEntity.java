package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetriveFromStockIssueEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String productode;
	private String productDesc;
	private String uomLongDesc;
	private String uomShortDesc;
	private String inventoryQty;
	private String requierdQty;
	private String productId;
	private String mrDtlId;
	private String bin;

}
