package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSalesContDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String saleValue;
	private	String salePercentage;
	private String finalCost;
	private	String totalBaseCode;
	private String uom;
}
