package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DocumentManagementAccessEntity implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private String deptCode;
	private String deptName;
	private String enabledDateTime;
	private String dmaId;
	

}
