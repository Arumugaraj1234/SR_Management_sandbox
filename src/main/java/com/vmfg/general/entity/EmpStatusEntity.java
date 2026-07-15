package com.vmfg.general.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpStatusEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String statusName;
	private String statusCode;
}
