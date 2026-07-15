package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductDtlsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String specification;
	private String productId;
	private String prodDesc;
	private String uom;
	private String make;
	private String material;

}
