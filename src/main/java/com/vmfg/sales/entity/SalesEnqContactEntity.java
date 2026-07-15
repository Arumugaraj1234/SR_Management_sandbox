package com.vmfg.sales.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesEnqContactEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String secId;
	private String masterId;
	private String contactName;
	private String contactEmail;
	private String contactNo;
	private String department;
	private boolean isPrimary;
}
