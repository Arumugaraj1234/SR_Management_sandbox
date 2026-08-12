package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubAreaExtnHistEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pkseHistId;
	private String elementHdr;
	private String elementDtl;
	private String specification;
	private String make;
	private String allocatedQty;
	private String allocatedvalue;
	private String source;
	private String empName;
	private String createdOn;
}
