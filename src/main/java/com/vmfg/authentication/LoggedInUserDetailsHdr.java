package com.vmfg.authentication;

import java.io.Serializable;

public class LoggedInUserDetailsHdr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String employeeLoggedInCount;
	private String employeeName;
	private String employeeId;
	public String getEmployeeLoggedInCount() {
		return employeeLoggedInCount;
	}
	public void setEmployeeLoggedInCount(String employeeLoggedInCount) {
		this.employeeLoggedInCount = employeeLoggedInCount;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	
	

}
