package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndentHdrEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String sbcCode;
	private String budgetValue;
	private String budgetAllocated;
	private String targetValue;

}
