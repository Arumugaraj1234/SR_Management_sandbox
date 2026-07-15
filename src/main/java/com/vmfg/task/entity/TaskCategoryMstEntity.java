package com.vmfg.task.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCategoryMstEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String tcCode;
	private String tcDesc;
	private String isActive;
	private String tenantId;
	private String ttCode;
}
