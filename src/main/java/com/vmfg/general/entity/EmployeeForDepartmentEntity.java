package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeForDepartmentEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String employeeId;
	private String employeeName;

}
