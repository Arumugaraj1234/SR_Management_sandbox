package com.vmfg.authentication;

import java.io.Serializable;

public class LoginUserInfoHdr implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String loginTime;
	private String empName;
	private String empId;
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}

}
