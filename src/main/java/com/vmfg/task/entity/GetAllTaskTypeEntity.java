package com.vmfg.task.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllTaskTypeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ttCode;
	private String ttDesc;
	private String deptCode;
	private String isActive;
	private String tenantId;
	private String deptName;
}
