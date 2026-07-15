package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class ProcessAssignedTeamEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String empId;
	private String employeeName;
	private String employeeDept;
	private String isActive;
	private String isPrimary;

}
