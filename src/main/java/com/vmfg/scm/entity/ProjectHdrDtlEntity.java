package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectHdrDtlEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String projectCode;
	private String transactionNo;
	private String customerName;
	private String projectName;
	private String seId;
}
