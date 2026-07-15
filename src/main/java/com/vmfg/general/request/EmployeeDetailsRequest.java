package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDetailsRequest {

	private String empName;
	private String password;
	private String status;
	private String designation;
	private String department;
	private String role;
	private String empId;
	private String tenantId;
	private String emailId;
	private String empCode;
	private String clientDesign;
	private String fromEmployee;
	private String toEmployee;
    private String indentDtlId;
	private String primary;
}
