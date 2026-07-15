package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectProgressEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String projectName;
	private String planStartDate;
	private String planEndDate;
	private String completedDate;
	private String projectCode;
	private String customerName;
	
}
