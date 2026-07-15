package com.vmfg.task.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEntryRemarksEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String teDtlId;
	private String remarks;
	private String empId;
	private String empName;
	private String dateTime;
}
