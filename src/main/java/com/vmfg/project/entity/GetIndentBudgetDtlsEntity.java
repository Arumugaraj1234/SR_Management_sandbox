package com.vmfg.project.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetIndentBudgetDtlsEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String indentCode;
	private String createdDate;
	private String pskDesc;
	private String pkDesc;
	
	
	
}
