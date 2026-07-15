package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialIssueDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String productCode;
	private String productDesc;
	private String uomLongDesc;
	private String uomShortDesc;
	private String availableQty;
	private String requestedQty;
	private String issuedQty;

}
