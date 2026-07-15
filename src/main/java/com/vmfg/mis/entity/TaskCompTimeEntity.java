package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCompTimeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String projName;
	private String activity;
	private String compPer;
	private String startDate;
	private String endDate;
	private String delay;
	private String actualDate;
	private String completedDate;
}
