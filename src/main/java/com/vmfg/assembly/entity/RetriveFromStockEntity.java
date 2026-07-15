package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetriveFromStockEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String availableQty;
	private String productode;
	private String productDesc;
	private String uomLongDesc;
	private String uomShortDesc;
	private String productId;
	private String pkaId;
	private String pksaId;
	private String pkDesc;
	private String pskDesc;
	private String invLocationDesc;
	private String invLocationCode;
	private String specification;
	private String make;

}
