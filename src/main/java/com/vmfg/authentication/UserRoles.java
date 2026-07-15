package com.vmfg.authentication;

import java.io.Serializable;
import java.util.List;

public class UserRoles implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userRoleID;
	private int userLoginID;
	private String roleCode;
	private String roleName;
	private String roleDescription;
	private String tenandID;
	
	
	List<UIModuleMst> uiModuleMstlist;
	
	public int getUserRoleID() {
		return userRoleID;
	}
	public void setUserRoleID(int userRoleID) {
		this.userRoleID = userRoleID;
	}
	public int getUserLoginID() {
		return userLoginID;
	}
	public void setUserLoginID(int userLoginID) {
		this.userLoginID = userLoginID;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	public String getTenandID() {
		return tenandID;
	}
	public void setTenandID(String tenandID) {
		this.tenandID = tenandID;
	}
	public List<UIModuleMst> getUiModuleMstlist() {
		return uiModuleMstlist;
	}
	public void setUiModuleMstlist(List<UIModuleMst> uiModuleMstlist) {
		this.uiModuleMstlist = uiModuleMstlist;
	}
	
	
	

}
