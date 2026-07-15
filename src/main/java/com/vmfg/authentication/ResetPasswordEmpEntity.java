package com.vmfg.authentication;

import java.io.Serializable;

public class ResetPasswordEmpEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int userloginid;
	private String userName;
	private String employeeid;
	private String empFirstName;
	private String empRoleName;
	private String employeeDesignation;
	private String isActive;
	private String tenantid;
	public int getUserloginid() {
		return userloginid;
	}
	public void setUserloginid(int userloginid) {
		this.userloginid = userloginid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}
	public String getEmpFirstName() {
		return empFirstName;
	}
	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}
	public String getEmpRoleName() {
		return empRoleName;
	}
	public void setEmpRoleName(String empRoleName) {
		this.empRoleName = empRoleName;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getTenantid() {
		return tenantid;
	}
	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}
	public String getEmployeeDesignation() {
		return employeeDesignation;
	}
	public void setEmployeeDesignation(String employeeDesignation) {
		this.employeeDesignation = employeeDesignation;
	}
	
	
	

}
