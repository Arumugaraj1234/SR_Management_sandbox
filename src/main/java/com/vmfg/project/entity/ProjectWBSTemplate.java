package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectWBSTemplate implements Serializable {

	private static final long serialVersionUID = 1L;
	private String pmTempId;
	private String tempName;
	private String milestoneName;
	private String respUser;
	private String responsibleDeptCode;
	private String tenantId;
	private String empName;
	private String deptName;

}
