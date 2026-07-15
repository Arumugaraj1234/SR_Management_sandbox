package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialReqDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String prodoctCode;
	private String productDesc;
	private String station;
	private String subAssy;
	private String uomLongDesc;
	private String uomShortDesc;
	private String availableQty;
	private String requestedQty;
	private String issuedQty;
	private String invenLocation;
	private String isCancelled;
	private String bin;
	private String specification;
	private String make;
	private String isCompleted;
}
