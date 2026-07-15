package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetIndentDtlProductCostEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String poCode;
	private String poDate;
	private String vendorName;
	private String unitRate;
}
