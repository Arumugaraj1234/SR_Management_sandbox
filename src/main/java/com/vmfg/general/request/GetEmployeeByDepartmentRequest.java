package com.vmfg.general.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetEmployeeByDepartmentRequest {
	private String tenantId;
	private String departmentId;
	private String employeeId;
	private String employmentStatusCode;

}
