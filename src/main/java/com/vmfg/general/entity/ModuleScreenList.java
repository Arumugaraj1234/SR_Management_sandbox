package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleScreenList implements Serializable {

	private static final long serialVersionUID = 1L;
	private String screenMstId;
	private String moduleDesc;
	private String screenDesc;
	private String screenDisplayName;
	private int isActive;
	private String roleId;

}
