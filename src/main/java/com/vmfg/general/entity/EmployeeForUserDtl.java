package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeForUserDtl implements Serializable {

	private static final long serialVersionUID = 1L;
	private String employeeId;
	private String employeeName;
	private String designCode;
	private String designation;
	private String userName;
	private String employeeStatus;
	private String userRole;
	private String deptCode;
	private String department;
	private String userRoleId;
	private String emplStatus;
	private String emailId;
	private String empCode;
	private String tenantId;
	private String clientDesign;

}
