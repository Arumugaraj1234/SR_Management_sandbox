package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EmployeemstdetailsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private String employeeId;
    private String employeeCode;
    private String employeeFirstName;
    private String designationCode;
    private String designationName;
    private String emailId;
    private String phoneNumber;
    private String employmentStatusCode;
    private String employeeStatusName;
    private String departmentCode;
    private String departmentName;
    private String dateOfJoining;
    private String lastWorkingDay;
    private String lastUpdatedUserId;
    private String lastUpdatedDateTime;
    private String approxSalary;

}
