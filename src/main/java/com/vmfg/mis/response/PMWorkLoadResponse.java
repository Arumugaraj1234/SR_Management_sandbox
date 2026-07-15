package com.vmfg.mis.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PMWorkLoadResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String empId;
	private String empCode;
	private String employeeName;
	private String invProject;
}
