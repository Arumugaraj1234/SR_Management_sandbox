package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductUnitCostEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productId;
	private String unitCost;
}
