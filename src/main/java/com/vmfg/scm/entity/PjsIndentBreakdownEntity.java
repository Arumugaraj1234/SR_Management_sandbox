package com.vmfg.scm.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PjsIndentBreakdownEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	// One row per distinct indent that contributes items to a PJS group.
	private String indentId;
	private String indentCode;
	private String indentType;
	private String subAssembly;
	private String partCount;

}
