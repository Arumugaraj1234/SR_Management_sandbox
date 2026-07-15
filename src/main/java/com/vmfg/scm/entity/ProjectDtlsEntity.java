package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDtlsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String projectCode;
	private String projectName;
	private String projectId;
	private String customerName;
	private String masterId;
}
