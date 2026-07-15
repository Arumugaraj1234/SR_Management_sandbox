package com.vmfg.design.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetKeySubAreaDtlEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private String pksaId;
	private String pmHdrId;
	private String pskId;
	private String pkaId;
	private String tenantId;
	private String pskDesc;
	private String pkDesc;
	private String pkCode;
	private String pskCode;
}
