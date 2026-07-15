package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class getPojCompDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String projName;
	private String projCode;
	private String planStart;
	private String planEnd;
	private String actName;
	private String customerName;
	private String actualStart;
//  private String actualEnd;
	private String completedDate;
	private String delay;
}
