package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class IndentGrpScpDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private int sno;
	private String igScpDItld;
	private String igScpId;
	private String igDtlId;
	private String indentDtlId;
	private String l1CurrencyType;
	private String l1ExchangeRate;
	private String l1UnitPriceFx;
	private String l1UnitPrice;
	private String l1ExtendedPrice;
	private String l1ExtendedPriceFx;
	private String l2CurrencyType;
	private String l2ExchangeRate;
	private String l2UnitPriceFx;
	private String l2UnitPrice;
	private String l2ExtendedPrice;
	private String l2ExtendedPriceFx;
	private String l3CurrencyType;
	private String l3ExchangeRate;
	private String l3UnitPriceFx;
	private String l3UnitPrice;
	private String l3ExtendedPrice;
	private String l3ExtendedPriceFx;
	private String finalL1UnitPriceFx;
	private String finalL1UnitPrice;
	private String finalL1ExtendedPrice;
	private String finalL1ExtendedPriceFx;
	private String finalL2UnitPriceFx;
	private String finalL2UnitPrice;
	private String finalL2ExtendedPrice;
	private String finalL2ExtendedPriceFx;
	private String finalL3UnitPriceFx;
	private String finalL3UnitPrice;
	private String finalL3ExtendedPrice;
	private String finalL3ExtendedPriceFx;
	private String tenantId;
	private String prodCode;
	private String prodDesc;
	private String uom;
	private String qty;
	private String dmId;
	private String prodSpec;
	private String weight;
	private String material;
	private String fileNameExtn;
	private String isPdf;

}
