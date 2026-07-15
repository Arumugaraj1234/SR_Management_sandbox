package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReInspectionVendorMasterEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String message;
	private String tenantId;

}
