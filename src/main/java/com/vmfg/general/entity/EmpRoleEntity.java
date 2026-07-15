package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpRoleEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String roleName;
	private String roleCode;
}
