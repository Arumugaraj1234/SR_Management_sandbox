package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSalesStageDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String description;
	private String val;
	private String seCount;
}
