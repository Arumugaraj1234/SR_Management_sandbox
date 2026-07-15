package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String assignedTo;
	private String cnt;
	private String pmHdrid;
	private String deptCode;
}
