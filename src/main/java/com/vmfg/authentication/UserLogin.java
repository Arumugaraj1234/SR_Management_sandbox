package com.vmfg.authentication;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int loginUserID;
	private String userName;
	private String empID;
	private String empRole;
	private String empFirstNane;
	private String empLastName;
	private String employeeDesignation;
	private String tenantID;
	private String jwtToken;
	private String roleid;
	private String roleName;
	private String roleCode;
	private String empDesignationCode;
	private String empDepDesc;
	private String depCode;
	private String status;
	private String statusMessage;
	
}
