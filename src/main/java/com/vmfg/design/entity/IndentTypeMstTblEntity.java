package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IndentTypeMstTblEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String indentTypeCode;
	private String indentTypeDesc;
	
}
