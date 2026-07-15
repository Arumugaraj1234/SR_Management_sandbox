package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectSubAreaExtnEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pkseId;
	private String sbExtnId;
	private String pkaId;
	private String allocatedQty;
	private String allocateVal;
	private String budgetQty;
	private String budgetValue;
}
