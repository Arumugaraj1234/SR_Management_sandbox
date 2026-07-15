package com.vmfg.mis.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ScmEmployeeIndentDtlsEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String pmHdrId;
	private String employeeId;
	private String projectCode;
	private String employee;
	private String totalIndentsAssigned;
	private String completedIndents;
}
