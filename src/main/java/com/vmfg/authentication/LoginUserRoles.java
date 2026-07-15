package com.vmfg.authentication;

import java.io.Serializable;

public class LoginUserRoles implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int	userRoleId	;
	private String	roleCode	;
	private String	roleName	;
	private String	roleDescription	;
	private boolean	isActive	;
	public int getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



}
