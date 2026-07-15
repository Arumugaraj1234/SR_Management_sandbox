package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BudgetKeyCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	private String keyCategory;
	private String keyCatCode;

}
