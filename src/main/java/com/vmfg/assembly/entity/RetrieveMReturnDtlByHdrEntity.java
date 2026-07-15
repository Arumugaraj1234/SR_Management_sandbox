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
	private String station;
	private String subAssy;
	private String qty;
	private String mrdId;
	private String isApproved;
	private String specification;
	private String make;
}
