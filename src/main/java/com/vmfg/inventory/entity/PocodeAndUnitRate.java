package com.vmfg.inventory.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PocodeAndUnitRate implements Serializable {

	private static final long serialVersionUID = 1L;

	private String poCode;
	private String unitRate;
}
