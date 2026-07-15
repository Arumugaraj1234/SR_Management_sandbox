package com.vmfg.master.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReasonCodeMasterEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String reasonCode;
	private String reasonCodeDesc;
	private String reasonType;
	private String reasonTypeDesc;

}
