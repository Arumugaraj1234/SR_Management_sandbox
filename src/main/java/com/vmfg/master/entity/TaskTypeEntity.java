package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskTypeEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ttDesc;
	private String ttCode;
	private String isActive;
}
