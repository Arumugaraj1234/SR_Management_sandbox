package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SumOfIndentHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String budgetCost;
	private String actualCost;
	private String targetCost;
}
