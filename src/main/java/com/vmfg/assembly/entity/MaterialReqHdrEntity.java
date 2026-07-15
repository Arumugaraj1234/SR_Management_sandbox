package com.vmfg.assembly.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialReqHdrEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String mrCode;
	private String requestedOn;
	private String employeeName;
	private String empId;
	private String completed;
	private String cancelled;
	private String reason;
	private String mrHdrId;
	private String productCount;
	private String requestType ;


}
