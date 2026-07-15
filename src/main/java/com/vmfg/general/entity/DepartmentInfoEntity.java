package com.vmfg.general.entity;

import java.io.Serializable;

public class DepartmentInfoEntity implements Serializable{


	private static final long serialVersionUID = 1L;
private  String departmentCode;
private  String departmentName;
private  String departmentEmail;
private  String departmentPhone;


public String getDepartmentCode() {
	return departmentCode;
}
public void setDepartmentCode(String departmentCode) {
	this.departmentCode = departmentCode;
}
public String getDepartmentName() {
	return departmentName;
}
public void setDepartmentName(String departmentName) {
	this.departmentName = departmentName;
}
public String getDepartmentEmail() {
	return departmentEmail;
}
public void setDepartmentEmail(String departmentEmail) {
	this.departmentEmail = departmentEmail;
}
public String getDepartmentPhone() {
	return departmentPhone;
}
public void setDepartmentPhone(String departmentPhone) {
	this.departmentPhone = departmentPhone;
}


}
